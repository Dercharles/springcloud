package com.com.dzjt.cbs.uam.gate.ratelimit.config;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by XT on 2017/9/23.
 */
public class DefaultUserPrincipal implements IUserPrincipal {
    @Override
    public String getName(HttpServletRequest request) {
        if(request.getUserPrincipal()==null) {
            return null;
        }
        return request.getUserPrincipal().getName();
    }
}
