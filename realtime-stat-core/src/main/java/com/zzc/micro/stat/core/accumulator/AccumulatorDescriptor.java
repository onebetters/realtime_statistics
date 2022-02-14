package com.zzc.micro.stat.core.accumulator;

import com.zzc.micro.stat.biz.events.Event;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Administrator
 */
@Value
public class AccumulatorDescriptor<T extends Event, V> implements Serializable {
    private static final long serialVersionUID = 177554826541253459L;

    /**
     * 任务ID
     */
    @Nonnull
    String id;

    /**
     * 唯一编号，对外使用
     */
    @Nonnull
    String code;

    /**
     * 任务名称
     */
    @Nonnull
    String name;

    /**
     * 周期（分钟）
     */
    int period;

    /**
     * 支持的事件类型
     */
    @Nonnull
    Class<T> eventType;

    /**
     * key值
     */
    @Nonnull
    Function<T, Collection<String>> keySupplier;

    /**
     * value值
     */
    @Nonnull
    Function<T, V> valueSupplier;

    /**
     * hash key后缀
     */
    @Nonnull
    Function<T, String> bucketSuffixSupplier;

    /**
     * 过滤
     */
    @Nonnull
    Predicate<T> filter;

    public static <T extends Event, V> Builder<T, V> builder(@Nonnull final String id,
            @Nonnull final String code,
            @Nonnull final String name,
            @Nonnegative final int period,
            @Nonnull final Class<T> eventType,
            @Nonnull final Function<T, Collection<String>> keySupplier,
            @Nonnull final Function<T, V> valueSupplier) {
        return new Builder<>(id, code, name, period, eventType, keySupplier, valueSupplier);
    }

    public static class Builder<T extends Event, V> implements org.apache.commons.lang3.builder.Builder<AccumulatorDescriptor<T, V>> {

        private final String id;
        private final String code;
        private final String name;
        private final int period;
        private final Class<T> eventType;
        private final Function<T, Collection<String>> keySupplier;
        private final Function<T, V> valueSupplier;
        private Function<T, String> bucketSuffixSupplier;
        private Predicate<T> filter;

        public Builder(@Nonnull String id,
                @Nonnull String code,
                @Nonnull String name,
                @Nonnegative int periodInMinutes,
                @Nonnull Class<T> eventType,
                @Nonnull Function<T, Collection<String>> keySupplier,
                @Nonnull Function<T, V> valueSupplier) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.period = periodInMinutes;
            this.eventType = eventType;
            this.keySupplier = keySupplier;
            this.valueSupplier = valueSupplier;
        }

        public Builder<T, V> withBucketSuffixSupplier(@Nullable Function<T, String> bucketSuffixSupplier) {
            this.bucketSuffixSupplier = bucketSuffixSupplier;
            return this;
        }

        public Builder<T, V> withFilter(@Nullable Predicate<T> filter) {
            this.filter = filter;
            return this;
        }

        @Override
        public AccumulatorDescriptor<T, V> build() {
            Validate.notBlank(id, "id must not be blank");
            Validate.notBlank(code, "name must not be blank");
            Validate.notBlank(name, "name must not be blank");
            Validate.isTrue(period >= 1, "periodInMinutes must greater or equal than 1");
            Validate.notNull(eventType, "eventType must not be null");
            Validate.notNull(keySupplier, "keySupplier must not be null");
            Validate.notNull(valueSupplier, "valueSupplier must not be null");

            if (Objects.isNull(bucketSuffixSupplier)) {
                bucketSuffixSupplier = e -> null;
            }
            if (Objects.isNull(filter)) {
                filter = e -> true;
            }
            return new AccumulatorDescriptor<>(id, code, name, period, eventType, keySupplier, valueSupplier, bucketSuffixSupplier, filter);
        }
    }
}
