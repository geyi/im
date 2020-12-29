package com.airing.im.config.zk;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Configuration
public class ZKClientConfig {

    @Value("${zookeeper.url}")
    private String zkUrl;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(zkUrl);
    }

    public static void main(String[] args) throws Exception {

        // zk是有session概念的，没有连接池的概念
        // watch：观察，回调
        // watch的注册只发生在 读类型调用，get，existed
        // 第一类：new zk时，传入的watch，这个watch是session级别的，跟path、node没有关系。

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        String url = "192.168.249.101:2181,192.168.249.102:2181,192.168.249.103:2181";
        String path = "/test";
        ZooKeeper zooKeeper = new ZooKeeper(url, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.EventType type = watchedEvent.getType();
                Event.KeeperState state = watchedEvent.getState();
                String path = watchedEvent.getPath();
                System.out.println(type + "|" + state + "|" + path);

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        System.out.println("node created");
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                }

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        countDownLatch.countDown();
                        System.out.println("connected");
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }
            }
        });

        countDownLatch.await();
        System.out.println(zooKeeper.getState().isConnected());

        String s = zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("create result:" + s);

        Stat stat = new Stat();
        byte[] data = zooKeeper.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.EventType type = watchedEvent.getType();
                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        System.out.println("data changed");
                        break;
                    case NodeChildrenChanged:
                        break;
                }
                try {
                    // watch参数传true时，默认会使用创建ZooKeeper对象时使用的watcher
                    // zooKeeper.getData(path, true, stat);
                    zooKeeper.getData(path, this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);

        System.out.println("get data:" + new String(data));

        Stat set02 = zooKeeper.setData(path, "test02".getBytes(), 0);
        System.out.println("set02 version:" + set02.getVersion());
        Stat set03 = zooKeeper.setData(path, "test03".getBytes(), set02.getVersion());
        System.out.println("set03 version:" + set03.getVersion());

        System.out.println("-------async start----------");
        zooKeeper.getData(path, false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("-------async call back----------");
                System.out.println(ctx.toString());
                System.out.println(new String(data));

            }

        }, "abc");
        System.out.println("-------async over----------");

        System.in.read();
    }
}
