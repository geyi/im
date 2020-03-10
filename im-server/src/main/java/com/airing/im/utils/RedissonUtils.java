package com.airing.im.utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName:      RedissonUtils
 * @Description:    操作redis的工具类
 * @Author:         GEYI
 * @CreateDate:     2019年03月23日 03:40
 * @Version:        1.0
 * @Copyright:      2019/3/23 yian Inc. All rights reserved.
 **/
@Component
public class RedissonUtils {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * @Title:         get
     * @Description:   获取一个字符串类型的值
     * @Author:        GEYI
     * @Date:          2019年03月23日 03:41
     * @Param:         [key]
     * @return:        java.lang.String
     **/
    public String get(String key) {
        RBucket rb = redissonClient.getBucket(key);
        Object obj = rb.get();
        if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    /**
     * @Title:         set
     * @Description:   保存一个具有过期时间的键值对，当过期时间小于零时键值对将永久保存
     * @Author:        GEYI
     * @Date:          2019年03月23日 03:42
     * @Param:         [key, value, timeout, timeUnit]
     * @return:        void
     **/
    public void set(String key, String value, long expireTime, TimeUnit timeUnit) {
        RBucket rb = redissonClient.getBucket(key);
        if (expireTime < 0) {
            rb.set(value);
        } else {
            rb.set(value, expireTime, timeUnit);
        }
    }

    /**
     * @Title:         set
     * @Description:   永久保存一个键值对
     * @Author:        GEYI
     * @Date:          2019年03月23日 03:44
     * @Param:         [key, value]
     * @return:        void
     **/
    public void set(String key, String value) {
        set(key, value, -1, TimeUnit.MILLISECONDS);
    }

    /**
     * @Title:         remove
     * @Description:   删除一个键值对
     * @Author:        GEYI
     * @Date:          2019年03月23日 03:44
     * @Param:         [key]
     * @return:        boolean
     **/
    public boolean remove(String key) {
        RBucket rb = redissonClient.getBucket(key);
        return rb.delete();
    }

    /**
     * @Title:         setnx
     * @Description:   将 key 的值设为 value ，当且仅当 key 不存在
     *                 若给定的 key 已经存在，则不做任何动作
     * @Author:        GEYI
     * @Date:          2019年04月12日 18:37
     * @Param:         [key, value]
     * @return:        boolean
     **/
    public boolean setnx(String key, String value) {
        RBucket rb = redissonClient.getBucket(key);
        return rb.trySet(value);
    }

    /**
     * @Title:         setnx
     * @Description:   具有过期时间的setnx
     * @Author:        GEYI
     * @Date:          2019年04月12日 18:40
     * @Param:         [key, value, expireTime, timeUnit]
     * @return:        boolean
     **/
    public boolean setnx(String key, String value, long expireTime, TimeUnit timeUnit) {
        RBucket rb = redissonClient.getBucket(key);
        return rb.trySet(value, expireTime, timeUnit);
    }

    public boolean sAdd(String key, String value) {
        RSet rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.add(value);
    }

    public boolean sAdd(String key, List list) {
        RSet rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.addAll(list);
    }

    public boolean sRem(String key, String value) {
        RSet rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.remove(value);
    }

    public Set<String> sMembers(String key) {
        RSet rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.readAll();
    }

    public int sCard(String key) {
        RSet rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.size();
    }

    public String sPop(String key) {
        RSet<String> rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.removeRandom();
    }

    public Set<String> sPop(String key, int count) {
        RSet<String> rSet = redissonClient.getSet(key, new StringCodec());
        return rSet.removeRandom(count);
    }

    public int sDiffStore(String dest, String key1, String key2) {
        RSet<String> rSet = redissonClient.getSet(dest, new StringCodec());
        return rSet.diff(key1, key2);
    }

    public boolean isExists(String key) {
        RBucket rb = redissonClient.getBucket(key);
        return rb.isExists();
    }

    public boolean isNotExists(String key) {
        return !isExists(key);
    }

    public long incr(String key) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key);
        return rAtomicLong.incrementAndGet();
    }

    public long incr(String key, long expireTime, TimeUnit timeUnit) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key);
        rAtomicLong.expire(expireTime, timeUnit);
        return rAtomicLong.incrementAndGet();
    }
}
