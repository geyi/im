package com.airing.im.redis;

import com.airing.im.IMApplicationTests;
import com.airing.im.utils.RedissonUtils;
import com.airing.utils.ThreadPoolUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisTest extends IMApplicationTests {
    @Autowired
    private RedissonUtils redissonUtils;

    @Test
    public void testIncr() {
        for (int i = 0; i < 100; i++) {
            ThreadPoolUtils.getSingle().execute(() -> {
                redissonUtils.incr("geyiIncr");
            });
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(redissonUtils.getIncrVal("geyiIncr"));
    }
}
