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
public class SumAccumulator<E extends Event> extends AbstractAccumulator<E, Number> {

    public SumAccumulator(@Nonnull AccumulatorDescriptor<E, Number> descriptor) {
        super(descriptor);
    }

    @Override
    protected void doAggregate(@Nonnull E event, @Nonnull String hashKey, @Nonnull String memberKey, @Nonnull Number memberValue) {
        RedisUtils.execute(RedisScripts.sumScript(), Collections.singletonList(hashKey), memberKey, memberValue.doubleValue(), this.defaultTtlMillis());
    }
}
