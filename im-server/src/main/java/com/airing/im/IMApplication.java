package com.airing.im;

import com.airing.im.config.app.AppConfig;
import com.airing.im.config.app.AppRegister;
import com.airing.im.server.ChatServer;
import com.airing.im.utils.SpringBeanFactory;
import com.airing.utils.ThreadPoolUtils;
import io.netty.channel.ChannelFuture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class IMApplication {
    @Autowired
    private AppConfig appConfig;

    public static void main(String[] args) {
        try {
            builderPid();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(IMApplication.class, args);
        startIMServer();
    }

    @Component
    private class Register implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            String ip = InetAddress.getLocalHost().getHostAddress();
//            String ip = "192.168.77.239";
            appConfig.setIp(ip);
            ThreadPoolUtils.getSingle().execute(new AppRegister(ip));
        }
    }

    private static void startIMServer() {
        int port = SpringBeanFactory.getBean(AppConfig.class).getWsPort();
        final ChatServer endpoint = new ChatServer();
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }

    private static void builderPid() throws IOException {
        File file = new File("im.pid");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(getPid().getBytes());
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }

}
