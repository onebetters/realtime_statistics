package com.zzc.micro.stat.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "stat")
public class StatProperties {

    private Collector collector = new Collector();

    private Aggregator aggregator = new Aggregator();

    private MeasureConfigure measure = new MeasureConfigure();

    @Data
    public static class Collector {
        private String type = "redisQuick";
    }

    @Data
    public static class Aggregator {

        private boolean async = true;
    }

    @Data
    public static class MeasureConfigure {

        private String measureFiles = "classpath:measures/*.json";
    }
}
