package com.zzc.micro.stat.core.event;

import com.zzc.micro.stat.biz.events.Event;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
public interface EventBus {

    <E extends Event> void send(@Nonnull final E event);
}
