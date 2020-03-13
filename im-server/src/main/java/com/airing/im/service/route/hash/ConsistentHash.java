package com.airing.im.service.route.hash;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConsistentHash<T> {
    private final HashFunction hashFunction;
    private final int factor; // 节点的复制因子，实际节点个数 * factor = 虚拟节点个数
    private final SortedMap<Long, T> circle = new TreeMap<>(); // 存储虚拟节点的hash值到真实节点的映射

    public ConsistentHash(HashFunction hashFunction, int factor, Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.factor = factor;
        for (T node : nodes) {
            add(node);
        }
    }

    /**
     * 不同的虚拟节点（i不同）有不同的hash值，但都对应同一个实际机器node
     * 虚拟node一般是均衡分布在环上的，数据存储在顺时针方向的虚拟node上
     *
     * @param node
     */
    public void add(T node) {
        for (int i = 0; i < factor; i++) {
            circle.put(hashFunction.hash(node.toString() + i), node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < factor; i++) {
            circle.remove(hashFunction.hash(node.toString() + i));
        }
    }

	/**
	 * 获得一个最近的顺时针节点，根据给定的key取Hash
	 * 然后再取得顺时针方向上最近的一个虚拟节点对应的实际节点
	 * 再从实际节点中取得数据
	 *
	 * @param key
	 * @return
	 */
	public T get(Object key) {
        if (circle.isEmpty()) {
			return null;
		}
		long hash = hashFunction.hash((String) key);
        if (!circle.containsKey(hash)) {
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public long getSize() {
        return circle.size();
    }

    public void testBalance() {
        Set<Long> sets = circle.keySet();
        SortedSet<Long> sortedSets = new TreeSet<>(sets);
        for (Long hashCode : sortedSets) {
            System.out.println(hashCode);
        }

        Iterator<Long> it = sortedSets.iterator();
        Iterator<Long> it2 = sortedSets.iterator();
        if (it2.hasNext()) {
			it2.next();
		}
        long keyPre, keyAfter;
        while (it.hasNext() && it2.hasNext()) {
            keyPre = it.next();
            keyAfter = it2.next();
            System.out.println(keyAfter - keyPre);
        }
    }

    public static void main(String[] args) {
        Set<String> nodes = new HashSet<String>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");

        ConsistentHash<String> consistentHash = new ConsistentHash<>(new HashFunction(), 2, nodes);
        consistentHash.add("D");

        System.out.println("hash circle size: " + consistentHash.getSize());
        System.out.println("location of each node are follows: ");
        consistentHash.testBalance();

        String userId = "66344835779617095681";
        System.out.print(userId + ": ");
        System.out.println(consistentHash.get(userId));

        consistentHash.remove("D");
        System.out.print(userId + ": ");
        System.out.println(consistentHash.get(userId));
    }
}
