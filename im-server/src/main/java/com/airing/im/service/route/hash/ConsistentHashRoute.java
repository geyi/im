package com.airing.im.service.route.hash;

import com.airing.im.service.route.Route;
import java.util.List;

public class ConsistentHashRoute implements Route {
    @Override
    public String getRoute(List<String> serverList, String key) {
        ConsistentHash<String> consistentHash = new ConsistentHash<>(new HashFunction(), 16, serverList);
        return consistentHash.get(key);
    }
}
