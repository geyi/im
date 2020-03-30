package com.airing.im.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class ThreadTaskHolder {
    private static Map<Future, Runnable> MAP = new ConcurrentHashMap<>(128);

    public static void put(Future key, Runnable value) {
        MAP.put(key, value);
    }

    public static Runnable get(Future key) {
        return MAP.get(key);
    }

    public static Map<Future, Runnable> getMAP() {
        return MAP;
    }

    public static void remove(Runnable value) {
        MAP.entrySet().stream().filter(entry -> entry.getValue() == value)
                .forEach(entry -> MAP.remove(entry.getKey()));
    }

    public static void print() {
        System.out.println(MAP);
    }

    public static boolean containsKey(Future key) {
        return MAP.containsKey(key);
    }
}
