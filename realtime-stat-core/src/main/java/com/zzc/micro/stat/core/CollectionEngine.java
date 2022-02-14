package com.zzc.micro.stat.core;

import com.zzc.micro.stat.biz.events.Event;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
public interface CollectionEngine {

    /**
     * 事件收集
     */
    <T extends Event> void accept(@Nonnull final T event);
}
