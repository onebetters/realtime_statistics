package com.zzc.micro.stat.core.config;

import com.zzc.micro.stat.core.CollectionEngine;
import com.zzc.micro.stat.core.InMemoryCollectionEngine;
import com.zzc.micro.stat.core.collector.Collector;
import com.zzc.micro.stat.core.collector.RedisCollector;
import com.zzc.micro.stat.core.collector.RedisQuickCollector;
import com.zzc.micro.stat.core.config.accumulator.AccumulatorManager;
import com.zzc.micro.stat.core.event.EventBus;
import com.zzc.micro.stat.core.event.InMemoryEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "stat", name = "collector.type", havingValue = "redis")
    public Collector redisCollector() {
        return new RedisCollector();
    }

    @Bean
    @ConditionalOnMissingBean(Collector.class)
    @ConditionalOnProperty(prefix = "stat", name = "collector.type", havingValue = "redisQuick", matchIfMissing = true)
    public Collector redisQuickCollector() {
        return new RedisQuickCollector();
    }

    @Bean
    @ConditionalOnMissingBean(EventBus.class)
    public EventBus eventBus(final AccumulatorManager accumulatorManager, final StatProperties properties) {
        return new InMemoryEventBus(accumulatorManager, properties);
    }

    @Bean
    @ConditionalOnMissingBean(CollectionEngine.class)
    public CollectionEngine collectionEngine(final Collector collector, final EventBus eventBus) {
        return new InMemoryCollectionEngine(collector, eventBus);
    }
}
