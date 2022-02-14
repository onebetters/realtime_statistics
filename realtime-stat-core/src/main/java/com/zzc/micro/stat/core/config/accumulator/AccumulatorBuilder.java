package com.zzc.micro.stat.core.config.accumulator;

import com.zzc.micro.stat.biz.events.Event;
import com.zzc.micro.stat.core.accumulator.Accumulator;
import com.zzc.micro.stat.core.accumulator.AccumulatorDescriptor;
import com.zzc.micro.stat.core.accumulator.AccumulatorImplFactory;
import com.zzc.micro.stat.core.config.event.EventManager;
import com.zzc.micro.stat.core.config.metadata.MeasureMetadata;
import com.zzc.micro.stat.core.supports.Spel;
import com.zzc.micro.stat.core.supports.StatHelpers;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Administrator
 */
public class AccumulatorBuilder implements Builder<Accumulator<?>> {

    private final MeasureMetadata measure;

    public AccumulatorBuilder(MeasureMetadata measure) {
        this.measure = measure;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Accumulator build() {
        final Class<? extends Event> eventType = this.eventType(measure.getEvent());

        final String[] keys = measure.collectKeys();
        final Function keySupplier = this.keySupplier(keys);
        final Function valueSupplier = this.valueSupplier(measure.getValue());

        final AccumulatorDescriptor.Builder builder = AccumulatorDescriptor.builder(measure.getId(),
                                                                                    measure.getCode(),
                                                                                    measure.getDesc(),
                                                                                    measure.getPeriod(),
                                                                                    eventType,
                                                                                    keySupplier,
                                                                                    valueSupplier);
        if (keys.length > 1) {
            final String[] hashKeyFields = ArrayUtils.subarray(keys, 0, keys.length - 1);
            builder.withBucketSuffixSupplier(this.bucketSuffixSupplier(hashKeyFields));
        }
        if (StringUtils.isNotBlank(measure.getCondition())) {
            builder.withFilter(this.filter(measure.getCondition()));
        }

        final Class<? extends Accumulator> aggregatorClass = AccumulatorImplFactory.find(measure.getAggregator())
                                                                                   .orElseThrow(() -> new AccumulatorImplNotFoundException(measure.getAggregator()));
        final Class[] parameterTypes = {AccumulatorDescriptor.class};
        final Constructor<? extends Accumulator> constructor;
        try {
            constructor = ReflectionUtils.accessibleConstructor(aggregatorClass, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new AccumulatorImplNotFoundException(measure.getAggregator());
        }
        return BeanUtils.instantiateClass(constructor, builder.build());
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> Class<E> eventType(final String classType) {
        return (Class<E>) EventManager.find(classType);
    }

    private <E extends Event> Function<E, Collection<String>> keySupplier(@Nonnull final String[] keys) {
        return event -> {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.singletonList(null);
            }

            final Object value = Spel.exec(keys[keys.length - 1], event);

            if (Objects.isNull(value)) {
                return null;
            }
            if (value instanceof Iterable) {
                final Iterable<?> iterable = (Iterable<?>) value;
                return StreamSupport.stream(iterable.spliterator(), false)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .distinct()
                        .collect(Collectors.toList());
            }
            if (value instanceof Iterator) {
                final Iterator<?> iterator = (Iterator<?>) value;
                return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .distinct()
                        .collect(Collectors.toList());
            }
            return Collections.singletonList(value.toString());
        };
    }

    private <E extends Event, V> Function<E, V> valueSupplier(@Nonnull final String valueField) {
        return event -> Spel.exec(valueField, event);
    }

    private <E extends Event> Function<E, String> bucketSuffixSupplier(@Nonnull final String[] hashFields) {
        return event -> StatHelpers.joinBucketSuffixKeys(Arrays.stream(hashFields)
                                                               .map(hashField -> expResultAsString(event, hashField))
                                                               .toArray(String[]::new));
    }

    private <T extends Event> String expResultAsString(final T event, @Nonnull final String exp) {
        final Object value = Spel.exec(exp, event);
        if (Objects.isNull(value)) {
            return StringUtils.EMPTY;
        }
        return value.toString();
    }

    private <E extends Event> Predicate<E> filter(@Nonnull final String condition) {
        return event -> Spel.exec(condition, event);
    }
}
