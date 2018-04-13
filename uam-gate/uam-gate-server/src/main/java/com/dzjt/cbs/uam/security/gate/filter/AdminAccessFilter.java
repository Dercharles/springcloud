package com.dzjt.cbs.uam.security.gate.filter;

import com.alibaba.fastjson.JSON;
import com.dzjt.cbs.uam.security.api.vo.authority.PermissionInfo;
import com.dzjt.cbs.uam.security.api.vo.log.LogInfo;
import com.dzjt.cbs.uam.security.api.vo.user.UserInfo;
import com.dzjt.cbs.uam.security.auth.client.config.ServiceAuthConfig;
import com.dzjt.cbs.uam.security.auth.client.config.UserAuthConfig;
import com.dzjt.cbs.uam.security.auth.client.jwt.ServiceAuthUtil;
import com.dzjt.cbs.uam.security.auth.client.jwt.UserAuthUtil;
import com.dzjt.cbs.uam.security.auth.common.util.jwt.IJWTInfo;
import com.dzjt.cbs.uam.security.common.context.BaseContextHandler;
import com.dzjt.cbs.uam.security.common.msg.auth.TokenErrorResponse;
import com.dzjt.cbs.uam.security.common.msg.auth.TokenForbiddenResponse;
import com.dzjt.cbs.uam.security.common.util.ClientUtil;
import com.dzjt.cbs.uam.security.gate.feign.ILogService;
import com.dzjt.cbs.uam.security.gate.feign.IUserService;
import com.dzjt.cbs.uam.security.gate.utils.DBLog;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * ${DESCRIPTION}
 *
 * @author XT
 * @date 2017-06-23 8:25
 */
@Component
@Slf4j
public class AdminAccessFilter extends ZuulFilter {
    @Autowired
    @Lazy
    private IUserService userService;
    @Autowired
    @Lazy
    private ILogService logService;

    @Value("${gate.ignore.startWith}")
    private String startWith;

    @Value("${zuul.prefix}")
    private String zuulPrefix;
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI().substring(zuulPrefix.length());
        final String method = request.getMethod();
//        System.out.println("----requestUri="+requestUri);
        BaseContextHandler.setToken(null);

        if(requestUri.equals("/auth/jwt/token")){   //登录日志
            this.setCurrentUserInfoSigninLog(ctx,userService.findByUsername(this.getUserName(request)));
            return null;
        }
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            return null;
        }
        IJWTInfo user ;
        try {
            user = getJWTUser(request, ctx);
        } catch (Exception e) {
            setFailedRequest(JSON.toJSONString(new TokenErrorResponse(e.getMessage())), 200);
            return null;
        }
        List<PermissionInfo> permissionIfs = userService.getPermissionByUsername(user.getUniqueName());
        if(permissionIfs.isEmpty()){
            setFailedRequest(JSON.toJSONString(new TokenForbiddenResponse("Token Forbidden!")), 200);
        }
        // 判断资源是否启用权限约束
        checkUserPermission(permissionIfs,method, ctx, user,requestUri);
        // 申请客户端密钥头
        ctx.addZuulRequestHeader(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        BaseContextHandler.remove();
        return null;
    }

    private String getUserName(HttpServletRequest request){
        String json = readBody(request);
        UserInfo info = JSON.parseObject(json, UserInfo.class);
        return info.getUsername();
    }

    private static String readBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String inputLine;
        BufferedReader br = null;
        try {
            br = request.getReader();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
        } catch (IOException e) {
            log.error("Failed to read body.", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
    private Stream<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo,boolean isAdmin) {
        return serviceInfo.parallelStream().filter(p->p.getUri()!=null).filter(new Predicate<PermissionInfo>() {
            @Override
            public boolean test(PermissionInfo permissionInfo) {
                String url = permissionInfo.getUri();
                String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                String regEx = "^" + uri + "$";
                return ((Pattern.compile(regEx).matcher(requestUri).find() || requestUri.equals(url))
                        || (isAdmin && (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url+"/")))
                        )&& method.equals(permissionInfo.getMethod());
            }
        });
    }

    /**
     * 记录增删改操作日志
     * @param ctx
     * @param user
     * @param pm
     */
    private void setCurrentUserInfoAndLog(RequestContext ctx, IJWTInfo user, PermissionInfo pm) {
        String host = ClientUtil.getClientIp(ctx.getRequest());
        ctx.addZuulRequestHeader("userId", user.getId());
        ctx.addZuulRequestHeader("userName", URLEncoder.encode(user.getName()));
        ctx.addZuulRequestHeader("userHost", ClientUtil.getClientIp(ctx.getRequest()));
        LogInfo logInfo = new LogInfo(pm.getCompanyId(),pm.getMenuId(),pm.getOptId(), pm.getMenu(),
                pm.getName(), pm.getUri(),pm.getMethod(), new Date(), user.getId(), user.getName(), host);
        logInfo.setCompanyName(pm.getCompanyName());
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }

    /**
     * 记录登录日志
     * @param ctx
     * @param info
     */
    private void setCurrentUserInfoSigninLog(RequestContext ctx,UserInfo info) {
        String host = ClientUtil.getClientIp(ctx.getRequest());
        ctx.addZuulRequestHeader("userHost", ClientUtil.getClientIp(ctx.getRequest()));
        LogInfo logInfo = new LogInfo(info.getBaseOrgId(),null,null, null,
                "登录", "/auth/jwt/token","post", new Date(), info.getId(), info.getName(), host);
        logInfo.setCompanyName(info.getCompanyName());
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }

    /**
     * 返回session中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(HttpServletRequest request, RequestContext ctx) throws Exception {
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        ctx.addZuulRequestHeader(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        return userAuthUtil.getInfoFromToken(authToken);
    }


    private void checkUserPermission(List<PermissionInfo> permissionIfs,String method, RequestContext ctx, IJWTInfo user,final String requestUri) {
        boolean isAdmin = user.getUniqueName().equals("admin");
        Stream<PermissionInfo> stream = getPermissionIfs(requestUri,method,permissionIfs,isAdmin);
        List<PermissionInfo> perms = stream.collect(Collectors.toList());
        if(perms.isEmpty()){
            setFailedRequest(JSON.toJSONString(new TokenForbiddenResponse("Token Forbidden!")), 200);
        }

        PermissionInfo current = null;
        Optional optional = perms.stream().filter(p->p.getUri().equals(requestUri)).findFirst();
        if(optional!=null && optional.isPresent()){
            current = (PermissionInfo)optional.get();
        }
        if(!isAdmin && current==null){
            log.warn("---forbidden requestUri="+requestUri);
            setFailedRequest(JSON.toJSONString(new TokenForbiddenResponse("Token Forbidden!")), 200);
        }
        if (!RequestMethod.GET.toString().equals(method)) {    //不记录查询
            current.setUri(requestUri);
            current.setMethod(method);
//            System.out.println("----current.uri:"+current.getUri()+", companyId:"+current.getCompanyId()+", method:"+current.getMethod());
            setCurrentUserInfoAndLog(ctx, user, current);
        }
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private void setFailedRequest(String body, int code) {
        log.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }

}
