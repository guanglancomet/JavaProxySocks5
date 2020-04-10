package com.guanglan.constant;

/**
 * @program: ProxySocks
 * @description: ProxyConstant
 * @author: 蓝Selice
 * @create: 2020-04-08 23:25
 **/
public class ProxyConstant {

    /**
     * 服务器监听端口
     */
    public static final Integer LISTEN_PORT = 10086;

    /**
     * Socket5 协议必须为0x05
     */
    public static final byte VER = (byte)0x05;
    /**
     * 保留字段RSV必须为0x00
     */
    public static final byte RSV = (byte)0x00;

    /**
     * 客户端服务器验证方式
     */
    public static final byte VALIDATION_NO_AUTHENTICATION_REQUIRED = (byte)0x00;

    public static final byte VALIDATION_GSSAPI = (byte)0x01;

    public static final byte VALIDATION_USERNAME_PASSWORD = (byte)0x02;

    public static final byte VALIDATION_TO_X_7F_IANA_ASSIGNED = (byte)0x03;

    public static final byte VALIDATION_TO_X_FE_RESERVED_FOR_PRIVATE_METHODS = (byte)0x04;

    public static final byte VALIDATION_NO_ACCEPTABLE_METHODS = (byte)0x05;

    /**
     * 客户端请求的类型，值长度也是1个字节，有三种类型；
     */
    public static final byte CMD_CONNECT = (byte)0x01;

    public static final byte CMD_BIND = (byte)0x02;

    public static final byte CMD_UDP = (byte)0x03;

    /**
     * 请求的远程服务器地址类型，值长度1个字节，有三种类型；
     */
    public static final byte ATYP_IPV4 = (byte)0x01;

    public static final byte ATYP_DOMAINNAME = (byte)0x03;

    public static final byte ATYP_IPV6 = (byte)0x04;

    /**
     * 响应状态码
     */
    public static final byte REP_SUCCESSED = (byte)0x00;

    public static final byte REP_GENERAL_SOCKS_SERVER_FAILURE = (byte)0x01;

    public static final byte REP_CONNECTION_NOT_ALLOWED_BY_RULESET = (byte)0x02;

    public static final byte REP_NETWORK_UNREACHABLE = (byte)0x03;

    public static final byte REP_HOST_UNREACHABLE = (byte)0x04;

    public static final byte REP_CONNECTION_REFUSED = (byte)0x05;

    public static final byte REP_TTL_EXPIRED = (byte)0x06;

    public static final byte REP_COMMAND_NOT_SUPPORTED = (byte)0x07;

    public static final byte REP_ADDRESS_TYPE_NOT_SUPPORTED = (byte)0x08;

    public static final byte REP_TO_0XFF_UNASSIGNED = (byte)0x09;




}
