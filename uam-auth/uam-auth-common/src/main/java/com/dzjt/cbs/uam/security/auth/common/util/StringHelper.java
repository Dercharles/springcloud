package com.dzjt.cbs.uam.security.auth.common.util;

/**
 * Created by XT on 2018/2/10.
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }
}
