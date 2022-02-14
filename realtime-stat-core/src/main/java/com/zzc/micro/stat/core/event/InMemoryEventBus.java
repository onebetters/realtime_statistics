package com.zzc.micro.stat.core.event;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.accumulator.Accumulator;
import com.zzc.micro.stat.core.config.StatProperties;
import com.zzc.micro.stat.core.config.accumulator.AccumulatorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 */
@Slf4j
public class InMemoryEventBus implements EventBus {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final Executor executor;
    private final AccumulatorManager accumulatorManager;
    private final boolean async;

    @Autowired
    public InMemoryEventBus(final AccumulatorManager accumulatorManager, final StatProperties properties) {
        this.accumulatorManager = accumulatorManager;
        this.async = properties.getAggregator().isAsync();
        this.executor = this.initExecutor();
    }

    private Executor initExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(processors * 5,
                processors * 10,
                180L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                r -> new Thread(r, "MetadataEvents-" + threadNumber.getAndIncrement()),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public <E extends Event> void send(@Nonnull final E event) {
        Arrays.stream(accumulatorManager.getAggregators())
                .filter(aggregator -> aggregator.support(event))
                .forEach(aggregator -> this.aggregate(aggregator, event));
    }

    @SuppressWarnings({"rawtypes"})
    private <E extends Event> void aggregate(@Nonnull final Accumulator accumulator, @Nonnull final E event) {
        if (async) {
            executor.execute(() -> this.aggregateWithoutException(accumulator, event));
        } else {
            this.aggregateWithoutException(accumulator, event);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <E extends Event> void aggregateWithoutException(@Nonnull final Accumulator accumulator, @Nonnull final E event) {
        try {
            accumulator.aggregate(event);
        } catch (Exception e) {
            log.error("Accumulator handle error, aggregator={}, event={}", accumulator.identifier(), event, e);
        }
    }
}
