package com.airing.im.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;


public class ServerCacheUtils {
    private static LoadingCache<String, String> cache;

    static {
        cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return null;
                    }
                });
    }

    public static void insertCache(String key) {
        cache.put(key, key);
    }

    public static void updateCache(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        cache.invalidateAll();
        for (String lis : list) {
            insertCache(lis);
        }
    }

    public static List<String> unsafeGetServerList() {
        List<String> list = new ArrayList<>();
        if (cache.size() == 0) {
            return list;
        }
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public static void main(String[] args) {
        ServerCacheUtils.insertCache("127.0.0.1:8888");
        System.out.println(JSONObject.toJSONString(ServerCacheUtils.unsafeGetServerList()));
    }
}
