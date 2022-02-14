package com.zzc.micro.stat.core.accumulator;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.supports.StatConstants;
import com.zzc.micro.stat.core.supports.StatHelpers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
public abstract class AbstractAccumulator<E extends Event, V> implements Accumulator<E> {

    private final AccumulatorDescriptor<E, V> descriptor;

    protected AbstractAccumulator(@Nonnull final AccumulatorDescriptor<E, V> descriptor) {
        this.descriptor = descriptor;
    }

    @Nonnull
    @Override
    public String identifier() {
        return descriptor.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean support(@Nonnull Event event) {
        final Class<?> eventType = descriptor.getEventType();
        return Objects.equals(eventType, event.getClass()) && this.doSupport((E) event);
    }

    @SuppressWarnings("unused")
    protected boolean doSupport(@Nonnull E event) {
        return true;
    }

    @Override
    public void aggregate(@Nonnull E event) {
        if (!descriptor.getFilter().test(event)) {
            log.debug("Can not match filter condition, aggregator={}, event={}", this.identifier(), event);
            return;
        }

        @Nonnull final LocalDateTime baseTime = event.getBaseTime();
        log.debug("baseTime={}, aggregator={}, event={}", baseTime, this.identifier(), event);

        @Nonnull final String hashKey = this.hashKey(event);
        @Nonnull final Collection<String> memberKeys = this.memberKey(event);
        @Nullable final V memberValue = this.memberValue(event);

        if (Objects.isNull(memberValue) || (memberValue instanceof CharSequence && StringUtils.isBlank(memberValue.toString()))) {
            log.debug("eventValue is null or blank, aggregator={}, event={}", this.identifier(), event);
            return;
        }

        for (final String memberKey : memberKeys) {
            this.doAggregate(event, hashKey, memberKey, memberValue);
        }
    }

    protected abstract void doAggregate(@Nonnull E event, @Nonnull final String hashKey, @Nonnull final String memberKey, @Nonnull final V memberValue);

    @Nonnull
    protected String hashKey(@Nonnull E event) {
        final LocalDateTime baseTime = event.getBaseTime();
        final long taskBucketKeySuffix = StatHelpers.periodOffset(descriptor.getPeriod(), baseTime);
        return StatHelpers.taskBucketKey(descriptor.getId(), descriptor.getPeriod(), taskBucketKeySuffix, descriptor.getBucketSuffixSupplier().apply(event));
    }

    @Nonnull
    private Collection<String> memberKey(@Nonnull E event) {
        if (CollectionUtils.isEmpty(descriptor.getKeySupplier().apply(event))) {
            return Collections.singleton(StatConstants.NULL_MEMBER_KEY);
        }
        return descriptor.getKeySupplier()
                .apply(event)
                .stream()
                .map(key -> StringUtils.defaultIfBlank(key, StatConstants.NULL_MEMBER_KEY))
                .distinct()
                .collect(Collectors.toList());
    }

    @Nullable
    private V memberValue(@Nonnull E event) {
        return descriptor.getValueSupplier().apply(event);
    }

    protected long defaultTtlMillis() {
        return StatHelpers.twoDaysLaterRandomMidnightTimeMillisToNow();
    }
}
