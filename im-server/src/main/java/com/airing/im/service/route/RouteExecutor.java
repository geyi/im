package com.airing.im.service.route;

import com.airing.im.service.route.hash.ConsistentHashRoute;
import java.util.ArrayList;
import java.util.List;

public class RouteExecutor {
    private Route route;

    public RouteExecutor(Route route) {
        this.route = route;
    }

    public String getServer(List<String> serverList, String key) {
        return this.route.getRoute(serverList, key);
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            RouteExecutor re = new RouteExecutor(new ConsistentHashRoute());
            List<String> serverList = new ArrayList<>();
            serverList.add("172.16.25.130:8888");
            serverList.add("172.16.25.131:8888");
            serverList.add("172.16.25.132:8888");
            serverList.add("172.16.25.133:8888");
            serverList.add("172.16.25.134:8888");
            serverList.add("172.16.25.135:8888");
            String userId = "66344835779617095681";
            System.out.println(re.getServer(serverList, userId));
        }
        System.out.println(System.nanoTime() - start);
    }
}
