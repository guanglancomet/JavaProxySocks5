package com.guanglan.utils;

import com.guanglan.constant.ProxyConstant;

import java.nio.ByteBuffer;

/**
 * @program: ProxySocks
 * @description:
 * @author: 蓝Selice
 * @create: 2020-04-09 11:53
 **/
public class ProxyUtil {

    public static byte[] buildCommandResponse(byte commandStatusCode) {
        ByteBuffer payload = ByteBuffer.allocate(100);
        payload.put(ProxyConstant.VER);
        payload.put(commandStatusCode);
        payload.put(ProxyConstant.RSV);
        payload.put(ProxyConstant.ATYP_DOMAINNAME);
        byte[] addressBytes = ProxyIpUtil.getServerIP().getBytes();
        payload.put((byte) addressBytes.length);
        payload.put(addressBytes);
        payload.put((byte) (((ProxyConstant.LISTEN_PORT & 0XFF00) >> 8)));
        payload.put((byte) (ProxyConstant.LISTEN_PORT & 0XFF));
        byte[] payloadBytes = new byte[payload.position()];
        payload.flip();
        payload.get(payloadBytes);
        return payloadBytes;
    }
}
