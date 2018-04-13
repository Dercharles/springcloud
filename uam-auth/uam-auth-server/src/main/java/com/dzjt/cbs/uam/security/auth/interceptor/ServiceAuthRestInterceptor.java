package com.dzjt.cbs.uam.security.auth.interceptor;

import com.dzjt.cbs.uam.security.auth.common.util.jwt.IJWTInfo;
import com.dzjt.cbs.uam.security.auth.configuration.ClientConfiguration;
import com.dzjt.cbs.uam.security.auth.service.AuthClientService;
import com.dzjt.cbs.uam.security.auth.util.client.ClientTokenUtil;
import com.dzjt.cbs.uam.security.common.exception.auth.ClientForbiddenException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by XT on 2018/2/12.
 */
@SuppressWarnings("ALL")
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(ServiceAuthRestInterceptor.class);

    @Autowired
    private ClientTokenUtil clientTokenUtil;
    @Autowired
    private AuthClientService authClientService;
    @Autowired
    private ClientConfiguration clientConfiguration;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader(clientConfiguration.getClientTokenHeader());
        if (StringUtils.isEmpty(token)) {
//            logger.warn("---ServiceAuthRestInterceptor2 token null! header:"+clientConfiguration.getClientTokenHeader());
            return super.preHandle(request, response, handler);
        }
        IJWTInfo infoFromToken = clientTokenUtil.getInfoFromToken(token);
        String uniqueName = infoFromToken.getUniqueName();
        for(String client: authClientService.getAllowedClient(clientConfiguration.getClientId())){
            if(client.equals(uniqueName)){
                return super.preHandle(request, response, handler);
            }
        }
        throw new ClientForbiddenException("Client is Forbidden!");
    }
}
