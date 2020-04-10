package com.guanglan.handler;

import com.guanglan.constant.ProxyConstant;
import com.guanglan.utils.ProxyIpUtil;
import com.guanglan.utils.ProxyUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @program: ProxySocks
 * @description:
 * @author: 蓝Selice
 * @create: 2020-04-09 00:12
 **/
public class ScoketHandler implements Runnable {

    public static final Logger logger = Logger.getLogger(ScoketHandler.class.getName());

    private Socket clientSocket;

    private String clientIp;

    private int clientPort;

    public ScoketHandler() {
    }

    public ScoketHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientIp = clientSocket.getInetAddress().getHostAddress();
        this.clientPort = clientSocket.getPort();
    }

    @Override
    public void run() {
        logger.info("验证流程开始 IP：" + clientIp + " Port：" + clientPort);
        try {
            clientValidateProcess();
            clientCommandHandler();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clientValidateProcess() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        byte[] buff = new byte[255];
        final int read = inputStream.read(buff, 0, 2);
        System.out.println(read);
        final byte VER = buff[0];
        final byte NMETHOD = buff[1];

        logger.info("验证流程 VER：" + VER + " NMETHOD：" + NMETHOD);

        if (VER != ProxyConstant.VER) {
            logger.info("exception msg : 请求类型必须为0x05");
            throw new RuntimeException("请求类型必须为0x05");
        }

        if (NMETHOD < 1) {
            logger.info("exception msg : 认证方法必须大于0");
            throw new RuntimeException("认证方法必须大于0");
        }
        inputStream.read(buff, 0, NMETHOD);
        //不认证 0x00
        buff[0] = ProxyConstant.VER;
        buff[1] = ProxyConstant.VALIDATION_NO_AUTHENTICATION_REQUIRED;
        outputStream.write(buff, 0, 2);
        outputStream.flush();
        //与客户端认证建立连接完成
        logger.info("与客户端认证建立连接完成");
    }

    private void clientCommandHandler() throws IOException {
        logger.info("开始处理客户端CMD命令流程");
        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        byte[] bytes = new byte[255];
        // 接收客户端命令
        logger.info("Socket 请求 IP ：" + clientSocket.getInetAddress().getHostAddress() + " 端口：" + clientSocket.getPort());
        is.read(bytes, 0, 4);

        final byte VER = bytes[0];
        final byte CMD = bytes[1];
        final byte RSV = bytes[2];
        final byte ATYP = bytes[3];

        logger.info("VER : " + bytes[0] + " CMD : " + bytes[1] + " RSV : " + bytes[2] + " ATYP : " + bytes[3]);

        if (RSV != ProxyConstant.RSV) {
            logger.info("exception msg : RSV必须为0x00");
            throw new RuntimeException("RSV必须为0x00");
        }
        if (VER != ProxyConstant.VER) {
            logger.info("exception msg : 请求类型必须为0x05");
            throw new RuntimeException("请求类型必须为0x05");
        }


        if ((CMD != ProxyConstant.CMD_CONNECT) && (CMD != ProxyConstant.CMD_BIND) && (CMD != ProxyConstant.CMD_UDP)) {
            logger.info("exception msg : 不支持CMD命令，CMD必须为CONNECT, BIND, UDP 其中之一");
            os.write(ProxyUtil.buildCommandResponse(ProxyConstant.REP_COMMAND_NOT_SUPPORTED));
            os.flush();
            return;
        }

        if ((ATYP != ProxyConstant.ATYP_IPV4) && (ATYP != ProxyConstant.ATYP_IPV6) && (ATYP != ProxyConstant.ATYP_DOMAINNAME)) {
            logger.info("exception msg : 不支持的ATYP，ATYP必须为IPV4, DOMAIN, IPV6 其中之一");
            os.write(ProxyUtil.buildCommandResponse(ProxyConstant.REP_ADDRESS_TYPE_NOT_SUPPORTED));
            os.flush();
            return;
        }

        String targetAddress = "";
        switch (ATYP) {
            case ProxyConstant.ATYP_DOMAINNAME:
                is.read(bytes, 0, 1);
                int domainLength = bytes[0];
                is.read(bytes, 0, domainLength);
                targetAddress = new String(Arrays.copyOfRange(bytes, 0, domainLength));
                break;
            case ProxyConstant.ATYP_IPV4:
                // 如果是ipv4的话使用固定的4个字节表示地址
                is.read(bytes, 0, 4);
                targetAddress = ProxyIpUtil.ipAddressBytesToString(bytes);
                break;
            case ProxyConstant.ATYP_IPV6:
                throw new RuntimeException("not support ipv6.");
        }

        is.read(bytes, 0, 2);
        int targetPort = ((bytes[0] & 0XFF) << 8) | (bytes[1] & 0XFF);

        if (CMD != ProxyConstant.CMD_CONNECT) {
            throw new RuntimeException("不支持UDP以及BIND");
        } else {
            Socket targetSocket = null;
            logger.info("目标服务器的IP地址：" + targetAddress + " 端口：" + targetPort);
            try {
                targetSocket = new Socket(targetAddress, targetPort);
            } catch (IOException e) {
                logger.info("建立SOCKET失败！目标服务器的IP地址：" + targetAddress + " 端口：" + targetPort);
                os.write(ProxyUtil.buildCommandResponse(ProxyConstant.REP_GENERAL_SOCKS_SERVER_FAILURE));
                os.flush();
                return;
            }
            os.write(ProxyUtil.buildCommandResponse(ProxyConstant.REP_SUCCESSED));
            os.flush();
            new FlowRelayDataHandler(clientSocket, targetSocket).doWork();
        }
    }
}
