package com.dzjt.cbs.uam.security.auth.service;


import java.util.List;

/**
 * Created by XT on 2018/2/10.
 */
public interface AuthClientService {
    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret);

    /**
     * 获取服务授权的客户端列表
     * @param serviceId
     * @return
     */
    public List<String> getAllowedClient(String serviceId);

    public void registryClient();

    public void validate(String clientId, String secret) throws Exception;
}
