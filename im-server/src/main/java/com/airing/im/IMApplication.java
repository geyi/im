package com.airing.im;

import com.airing.im.config.app.AppRegister;
import com.airing.utils.ThreadPoolUtils;
import java.net.InetAddress;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class IMApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class, args);
    }

    @Component
    private static class Register implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            String ip = InetAddress.getLocalHost().getHostAddress();
            ThreadPoolUtils.getSingle().execute(new AppRegister(ip));
        }
    }

}
