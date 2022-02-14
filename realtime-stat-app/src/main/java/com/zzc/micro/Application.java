package com.zzc.micro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 * @author Administrator
 */
@Slf4j
@EnableAspectJAutoProxy
@EnableScheduling
@RestController
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws UnknownHostException {
        final Environment env = SpringApplication.run(Application.class, args).getEnvironment();
        // @formatter:off
        log.info("Access URLs:\n" +
                         "----------------------------------------------------------\n" +
                         "\tLocal: \t\thttp://127.0.0.1:{}\n" +
                         "\tExternal: \thttp://{}:{}\n" +
                         "----------------------------------------------------------",
                 env.getProperty("server.port"),
                 InetAddress.getLocalHost().getHostAddress(),
                 env.getProperty("server.port"));
        // @formatter:on
    }

    @RequestMapping({"/", "/index"})
    public String index() {
        return "<h1>It works!</h1>";
    }
}
