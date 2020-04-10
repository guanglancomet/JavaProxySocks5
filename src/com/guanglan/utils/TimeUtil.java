package com.guanglan.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: ProxySocks
 * @description:
 * @author: 蓝Selice
 * @create: 2020-04-08 23:26
 **/
public class TimeUtil {

    private static final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String now(){
        final String now = LocalDateTime.now().format(yyyyMMddHHmmss);
        return now;
    }

}
