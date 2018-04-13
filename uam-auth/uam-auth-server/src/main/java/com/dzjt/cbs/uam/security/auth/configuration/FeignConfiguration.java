package com.dzjt.cbs.uam.security.auth.configuration;

import com.dzjt.cbs.uam.security.auth.interceptor.ClientTokenInterceptor;
import com.dzjt.cbs.uam.security.auth.interceptor.ClientTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by XT on 2018/2/12.
 */
@Configuration
public class FeignConfiguration {
    @Bean
    ClientTokenInterceptor getClientTokenInterceptor(){
        return new ClientTokenInterceptor();
    }
}
