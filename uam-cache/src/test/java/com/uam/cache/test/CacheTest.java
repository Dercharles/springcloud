package com.uam.cache.test;

import com.uam.cache.EnableAceCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by XT on 2018/2/21.
 */
@SpringBootApplication
@EnableAceCache
public class CacheTest {
    public static void main(String args[]) {
        SpringApplication app = new SpringApplication(CacheTest.class);
        app.run(args);
    }

}
