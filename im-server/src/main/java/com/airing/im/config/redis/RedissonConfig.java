package com.airing.im.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient getRedisson() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("redisson.yml");
        Config config = Config.fromYAML(classPathResource.getFile());
        config.useSingleServer();

        RedissonClient redisson = Redisson.create(config);

        return redisson;
    }

}
