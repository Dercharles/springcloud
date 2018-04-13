package com.dzjt.cbs.uam.security.gate;


import com.com.dzjt.cbs.uam.gate.ratelimit.EnableAceGateRateLimit;
import com.com.dzjt.cbs.uam.gate.ratelimit.config.IUserPrincipal;
import com.dzjt.cbs.uam.security.gate.config.UserPrincipal;
import com.dzjt.cbs.uam.security.gate.utils.DBLog;
import com.dzjt.cbs.uam.security.auth.client.EnableAceAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by XT on 2017/6/2.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.dzjt.cbs.uam.security.auth.client.feign","com.dzjt.cbs.uam.security.gate.feign"})
@EnableZuulProxy
@EnableScheduling
@EnableAceAuthClient
@EnableAceGateRateLimit
public class GateBootstrap {
    public static void main(String[] args) {
        DBLog.getInstance().start();
        SpringApplication.run(GateBootstrap.class, args);
    }

    @Bean
    @Primary
    IUserPrincipal userPrincipal(){
        return new UserPrincipal();
    }
}
