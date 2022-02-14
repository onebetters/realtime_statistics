package com.zzc.micro.stat.core.collector;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.supports.StatHelpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * @author Administrator
 */
@Slf4j
@Component
public abstract class QuickCollector implements Collector {

    @Override
    public <T extends Event> boolean collect(@Nonnull T event) {
        log.debug("Start add event={}", event);

        final String key = StatHelpers.eventBucketKey(event.getClass(), event.getBaseTime());
        final String id = event.getIdentifier();
        final boolean save = this.save(key, id);
        log.debug("Finish add event={}", event);

        return save;
    }

    abstract boolean save(final String key, final String id);
}
