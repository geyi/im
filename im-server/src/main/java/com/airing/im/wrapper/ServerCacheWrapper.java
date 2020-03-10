package com.airing.im.wrapper;

import com.airing.im.config.app.AppConfig;
import com.airing.im.utils.ServerCacheUtils;
import com.airing.im.utils.ZKUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ServerCacheWrapper {
    private final Logger log = LoggerFactory.getLogger(ServerCacheWrapper.class);

    @Autowired
    private ZKUtils zkUtils;
    @Autowired
    private AppConfig appConfig;

    public List<String> getServerList() {
        List<String> serverList = ServerCacheUtils.unsafeGetServerList();
        if (CollectionUtils.isEmpty(serverList)) {
            serverList = zkUtils.getChildren(appConfig.getZkAppRoot());
            ServerCacheUtils.updateCache(serverList);
        }
        return serverList;
    }
}
