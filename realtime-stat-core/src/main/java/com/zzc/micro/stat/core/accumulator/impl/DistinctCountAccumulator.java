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
 * 去重数量统计（精确统计），原则上需要存储原始value值系列信息，底层采用roaring bitmap算法尽量减少空间占用。
 * 一条数据，理论上可以占用几M到几十M（最小占用，几个bytes，最大占用，256M）
 * <p>
 * 使用建议：
 * 如果V（value）值能转换成唯一不重复数字的，强烈建议直接转数字；
 * 否则底层将是用BKDRHash算法转hashcode数字，理论上有极低的概率可能会产生hash碰撞，最终导致统计不是100%准确。
 *
 * @author Administrator
 */
@Slf4j
@SuppressWarnings("unused")
public class DistinctCountAccumulator<E extends Event, V> extends AbstractAccumulator<E, V> {

    public DistinctCountAccumulator(@Nonnull AccumulatorDescriptor<E, V> descriptor) {
        super(descriptor);
    }

    @Override
    protected void doAggregate(@Nonnull E event, @Nonnull String hashKey, @Nonnull String memberKey, @Nonnull V memberValue) {
        RedisUtils.execute(RedisScripts.distinctCountScript(), Collections.singletonList(hashKey), memberKey, memberValue, this.defaultTtlMillis());
    }
}
