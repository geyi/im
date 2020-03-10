package com.airing.im.utils;

import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZKUtils {
    private final Logger log = LoggerFactory.getLogger(ZKUtils.class);

    @Autowired
    private ZkClient zkClient;

    public void createAppNode(String appName) {
        boolean exists = zkClient.exists(appName);
        if (!exists) {
            zkClient.createPersistent(appName);
            log.info("{}创建成功", appName);
        } else {
            log.info("{}已存在，不再重复创建", appName);
        }
    }

    public void createEphemeral(String path) {
        zkClient.createEphemeral(path);
        log.info("{}临时目录创建成功", path);
    }

    public void monitorApp(String appName) {
        zkClient.subscribeChildChanges(appName, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                log.info("更新缓存parentPath={}，currentChilds={}", parentPath, currentChilds.toString());
                ServerCacheUtils.updateCache(currentChilds);
            }
        });
    }

    public List<String> getChildren(String appName) {
        List<String> childs = zkClient.getChildren(appName);
        return childs;
    }
}
