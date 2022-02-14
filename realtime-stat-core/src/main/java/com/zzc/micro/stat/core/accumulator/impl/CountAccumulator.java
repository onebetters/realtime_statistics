package com.zzc.micro.stat.core.accumulator.impl;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.accumulator.AbstractAccumulator;
import com.zzc.micro.stat.core.accumulator.AccumulatorDescriptor;
import com.zzc.micro.stat.core.supports.scripts.RedisScripts;
import com.zzc.micro.stat.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * @author Administrator
 */
@Slf4j
@SuppressWarnings("unused")
public class CountAccumulator<E extends Event, V> extends AbstractAccumulator<E, V> {

    public CountAccumulator(@Nonnull final AccumulatorDescriptor<E, V> descriptor) {
        super(descriptor);
    }

    @Override
    protected void doAggregate(@Nonnull E event, @Nonnull String hashKey, @Nonnull String memberKey, @Nonnull V memberValue) {
        RedisUtils.execute(RedisScripts.sumScript(), Collections.singletonList(hashKey), memberKey, 1, this.defaultTtlMillis());
    }
}
