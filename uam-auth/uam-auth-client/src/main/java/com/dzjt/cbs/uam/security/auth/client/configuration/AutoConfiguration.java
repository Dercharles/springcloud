package com.dzjt.cbs.uam.security.auth.client.configuration;

import com.dzjt.cbs.uam.security.auth.client.config.ServiceAuthConfig;
import com.dzjt.cbs.uam.security.auth.client.config.UserAuthConfig;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by XT on 2018/2/15.
 */
@Configuration
@ComponentScan({"com.dzjt.cbs.uam.security.auth.client","com.dzjt.cbs.uam.security.auth.common.event"})
@RemoteApplicationEventScan(basePackages = "com.dzjt.cbs.uam.security.auth.common.event")
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

}
