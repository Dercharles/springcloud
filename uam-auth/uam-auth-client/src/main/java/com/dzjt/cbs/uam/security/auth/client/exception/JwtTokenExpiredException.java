package com.dzjt.cbs.uam.security.auth.client.exception;

/**
 * Created by XT on 2018/2/15.
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}
