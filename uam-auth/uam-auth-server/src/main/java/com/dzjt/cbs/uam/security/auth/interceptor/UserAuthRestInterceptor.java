package com.dzjt.cbs.uam.security.auth.interceptor;

import com.dzjt.cbs.uam.security.auth.common.util.jwt.IJWTInfo;
import com.dzjt.cbs.uam.security.auth.configuration.UserConfiguration;
import com.dzjt.cbs.uam.security.auth.util.user.JwtTokenUtil;
import com.dzjt.cbs.uam.security.common.context.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by XT on 2018/2/10.
 */
@Slf4j
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(UserAuthRestInterceptor.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserConfiguration userConfiguration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader(userConfiguration.getUserTokenHeader());
        if (StringUtils.isEmpty(token)) {
//            log.warn("---UserAuthRestInterceptor2 token null! header:"+userConfiguration.getUserTokenHeader());
            return super.preHandle(request, response, handler);
        }
        IJWTInfo infoFromToken = jwtTokenUtil.getInfoFromToken(token);
        BaseContextHandler.setUsername(infoFromToken.getUniqueName());
        BaseContextHandler.setName(infoFromToken.getName());
        BaseContextHandler.setUserID(infoFromToken.getId());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
