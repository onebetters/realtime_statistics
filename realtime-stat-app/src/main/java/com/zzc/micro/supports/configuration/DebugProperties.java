package com.zzc.micro.supports.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 开发本机调试时配置项
 * 1、禁用定时任务
 * 2、禁用mq消费
 * 3、禁用dubbo provider  未完成
 *
 * <p>Filename: com.zzc.micro.supports.configuration.DebugProperties.java</p>
 * <p>Date: 2022-06-27, 周一, 19:15:47.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "debug")
public class DebugProperties {

    private JmsConsumer jmsConsumer;
    private Task        task;

    @Data
    public static class JmsConsumer {
        private boolean enabled = true;
    }

    @Data
    public static class Task {
        private boolean enabled = true;
    }
}
