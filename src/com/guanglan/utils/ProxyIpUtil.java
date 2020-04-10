package com.guanglan.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: ProxySocks
 * @description:
 * @author: 蓝Selice
 * @create: 2020-04-09 00:48
 **/
public class ProxyIpUtil {

    public static String getServerIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ipAddressBytesToString(byte[] ipAddressBytes) {
        return (ipAddressBytes[0] & 0XFF) + "." + (ipAddressBytes[1] & 0XFF) + "." + (ipAddressBytes[2] & 0XFF) + "." + (ipAddressBytes[3] & 0XFF);
    }

    public static int port(byte[] bytes){
        int targetPort = ((bytes[0] & 0XFF) << 8) | (bytes[1] & 0XFF);
        return targetPort;
    }
}
