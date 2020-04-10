package com.guanglan.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @program: ProxySocks
 * @description:
 * @author: 蓝Selice
 * @create: 2020-04-09 13:19
 **/
public class FlowRelayDataHandler{

    public static final Logger logger = Logger.getLogger(FlowRelayDataHandler.class.getName());

    private Socket clientSocket = null;

    private Socket targetSocket = null;

    public FlowRelayDataHandler() {}

    public FlowRelayDataHandler(Socket clientSocket, Socket targetSocket) {
        this.clientSocket = clientSocket;
        this.targetSocket = targetSocket;
    }


    public void doWork() {
        logger.info("开始处理数据交换流程");
        //客户端输入输出流
        OutputStream clientOS = null;
        InputStream clientIS = null;
        //目标服务器输入输出流
        OutputStream targetOS = null;
        InputStream targetIS = null;

        byte[] buff = new byte[1024 * 512];

        try {
            clientOS = clientSocket.getOutputStream();
            clientIS = clientSocket.getInputStream();

            targetOS = targetSocket.getOutputStream();
            targetIS = targetSocket.getInputStream();

            while (true){
                while (isAvailable(clientIS)){
                    targetOS.write(buff, 0, clientIS.read(buff));
                    targetOS.flush();
                }


                while (isAvailable(targetIS)){
                    clientOS.write(buff, 0, targetIS.read(buff));
                    clientOS.flush();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (clientIS != null){
                    clientIS.close();
                }
                if (clientOS != null){
                    clientOS.close();
                }
                if (targetIS != null){
                    targetIS.close();
                }
                if (targetOS != null){
                    targetOS.close();
                }
                if (clientSocket != null){
                    clientSocket.close();
                }
                if (targetSocket != null){
                    targetSocket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private boolean isAvailable(InputStream inputStream){
        try {
            return inputStream.available() != 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
