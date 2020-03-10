package com.airing.im.config.app;

import com.airing.im.utils.SpringBeanFactory;
import com.airing.im.utils.ZKUtils;

public class AppRegister implements Runnable {
    private ZKUtils zkUtils;
    private AppConfig appConfig;
    private String appName;
    private String ip;
    private int port;

    public AppRegister(String ip) {
        this.zkUtils = SpringBeanFactory.getBean(ZKUtils.class);
        this.appConfig = SpringBeanFactory.getBean(AppConfig.class);
        this.appName = this.appConfig.getAppName();
        this.ip = ip;
        this.port = this.appConfig.getPort();
    }

    @Override
    public void run() {
        StringBuilder path = new StringBuilder();
        path.append("/");
        path.append(this.appName);
        String rootPath = path.toString();
        zkUtils.createAppNode(rootPath);
        path.append("/");
        path.append(this.ip);
        path.append(":");
        path.append(port);
        zkUtils.createEphemeral(path.toString());
        zkUtils.monitorApp(rootPath);
    }
}
