package com.airing.im.config.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${server.port}")
    private int port;
    @Value("${zookeeper.app-root}")
    private String zkAppRoot;

    public String getAppName() {
        return appName;
    }

    public int getPort() {
        return port;
    }

    public String getZkAppRoot() {
        return zkAppRoot;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "appName='" + appName + '\'' +
                ", port=" + port +
                ", zkAppRoot='" + zkAppRoot + '\'' +
                '}';
    }
}
