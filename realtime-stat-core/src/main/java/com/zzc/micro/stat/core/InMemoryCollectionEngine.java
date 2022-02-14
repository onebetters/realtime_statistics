package com.zzc.micro.stat.core;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.collector.Collector;
import com.zzc.micro.stat.core.event.EventBus;
import lombok.extern.slf4j.Slf4j;
import com.zzc.micro.stat.utils.DateTimeUtils;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Slf4j
public class InMemoryCollectionEngine implements CollectionEngine {

    private final Collector collector;
    private final EventBus  eventBus;

    public InMemoryCollectionEngine(Collector collector, EventBus eventBus) {
        this.collector = collector;
        this.eventBus = eventBus;
    }

    /**
     * 事件收集
     */
    @Override
    public <T extends Event> void accept(@Nonnull final T event) {
        final LocalDateTime eventTime = event.getBaseTime();
        final LocalDateTime yesterdayStartTime = DateTimeUtils.atStartOfDay(DateTimeUtils.yesterday());
        if (eventTime.isBefore(yesterdayStartTime)) {
            log.debug("event base time is out of date, would not be handle, event={}", event);
            return;
        }

        final boolean addEvent = collector.collect(event);
        if (addEvent) {
            eventBus.send(event);
        }
    }
}
