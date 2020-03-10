package com.airing.im.config.zk;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZKClientConfig {

    @Value("${zookeeper.url}")
    private String zkUrl;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(zkUrl);
    }
}
