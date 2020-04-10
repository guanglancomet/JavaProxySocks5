package com.guanglan.server;

import com.guanglan.constant.ProxyConstant;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import com.guanglan.handler.ScoketHandler;
import com.guanglan.utils.TimeUtil;

/**
 * @program: ProxySocks
 * @description: ProxyServer
 * @author: 蓝Selice
 * @create: 2020-04-08 23:22
 **/
public class ProxyServer {
    public static final Logger logger = Logger.getLogger(ProxyServer.class.getName());
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(ProxyConstant.LISTEN_PORT);
        //服务端IP地址
        logger.info("Socket服务已启动，正在监听" + ProxyConstant.LISTEN_PORT + "端口");
        for (;;){
            Socket socket = serverSocket.accept();
            new Thread(new ScoketHandler(socket), "Thread main -- Working -- " + TimeUtil.now()).start();
        }

    }

}
