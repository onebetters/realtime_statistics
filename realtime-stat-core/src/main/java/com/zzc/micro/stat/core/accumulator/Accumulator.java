package com.zzc.micro.stat.core.accumulator;

import com.zzc.micro.stat.biz.events.Event;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
public interface Accumulator<E extends Event> {

    @Nonnull
    String identifier();

    boolean support(@Nonnull Event source);

    void aggregate(@Nonnull E event);
}
