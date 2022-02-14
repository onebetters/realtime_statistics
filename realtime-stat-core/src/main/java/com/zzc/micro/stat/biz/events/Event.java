package com.zzc.micro.stat.biz.events;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface Event {

    @Nonnull
    String getIdentifier();

    @Nonnull
    LocalDateTime getBaseTime();
}
