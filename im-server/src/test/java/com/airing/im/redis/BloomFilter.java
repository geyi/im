package com.airing.im.redis;

import com.airing.im.IMApplicationTests;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

public class BloomFilter extends IMApplicationTests {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() {
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter("USER_IDS");
        bloomFilter.tryInit(100L, 0.03);
        for (int i = 0; i < 10; i++) {
            bloomFilter.add(i + 1);
        }
        System.out.println(bloomFilter.contains(1));
        System.out.println(bloomFilter.contains(10));
        System.out.println(bloomFilter.contains(11));
    }
}
