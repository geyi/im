package com.airing.im.zk;

import com.airing.im.IMApplicationTests;
import com.airing.im.config.app.AppConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ZKTest extends IMApplicationTests {
    private final Logger log = LoggerFactory.getLogger(ZKTest.class);

    @Autowired
    private ZkClient zkClient;
    @Autowired
    private AppConfig appConfig;

    @Test
    public void test() {
        String zkRoot = "/" + appConfig.getAppName();
        boolean exists = zkClient.exists(zkRoot);
        if (!exists) {
            log.info("im is not exists");
            zkClient.createPersistent(zkRoot);
        } else {
            log.info("im is exists");
        }

        /* 注册自己 */
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }
        String path = zkRoot + "/" + ip + ":" + appConfig.getPort();
        zkClient.createEphemeral(path);
        log.info("注册zookeeper成功，msg={}", path);

        /* 注册监听 */
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                log.info("清除/更新本地缓存 parentPath={}，currentChilds={}", parentPath, currentChilds.toString());

                //更新所有缓存/先删除 再新增
                // serverCache.updateCache(currentChilds);
            }
        });
    }
}
