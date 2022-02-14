package com.zzc.micro.stat.core.collector;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.supports.StatHelpers;
import com.zzc.micro.stat.core.supports.scripts.RedisScripts;
import com.zzc.micro.stat.utils.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Administrator
 */
@Slf4j
public class RedisCollector implements Collector {

    @Override
    public <T extends Event> boolean collect(@Nonnull T event) {
        log.debug("Start add event={}", event);

        final String key = StatHelpers.eventBucketKey(event.getClass(), event.getBaseTime());
        final String id = event.getIdentifier();

        return RedisUtils.execute(RedisScripts.addHashScript(), Arrays.asList(key, id), event, this.defaultTtlMillis());
    }
}
