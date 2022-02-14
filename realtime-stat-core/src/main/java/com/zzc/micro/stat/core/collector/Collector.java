package com.zzc.micro.stat.core.collector;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.supports.StatHelpers;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
public interface Collector {

    <T extends Event> boolean collect(@Nonnull T event);

    default long defaultTtlMillis() {
        return StatHelpers.twoDaysLaterRandomMidnightTimeMillisToNow();
    }
}
