package com.airing.im.config.app;

import com.airing.im.service.route.RouteExecutor;
import com.airing.im.service.route.hash.ConsistentHashRoute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public RouteExecutor route() {
        RouteExecutor re = new RouteExecutor(new ConsistentHashRoute());
        return re;
    }
}
