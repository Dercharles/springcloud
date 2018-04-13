package com.com.dzjt.cbs.uam.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by XT on 2017/9/23.
 */
public interface IUserPrincipal {
    String getName(HttpServletRequest request);
}
