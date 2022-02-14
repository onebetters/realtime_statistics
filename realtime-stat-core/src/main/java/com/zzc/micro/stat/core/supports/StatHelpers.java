package com.zzc.micro.stat.core.supports;

import com.zzc.micro.stat.core.supports.scripts.RedisScripts;
import com.zzc.micro.stat.utils.DateTimeUtils;
import com.zzc.micro.stat.utils.redis.RedisUtils;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@UtilityClass
public class StatHelpers {

    /**
     * @return 事件源redis key
     */
    public <E> String eventBucketKey(@Nonnull final Class<E> clazz, @Nonnull final LocalDateTime baseTime) {
        return String.join(StatConstants.KEY_DELIMITER, StatConstants.PREFIX_EVENT, eventType(clazz), DateTimeUtils.parse(baseTime, "yyyyMMdd"));
    }

    private <E> String eventType(final Class<E> clazz) {
        return clazz.getSimpleName();
    }

    /**
     * @return 具体任务结果存储redis key
     */
    public String taskBucketKey(@Nonnull final String taskId, final int periodInMinutes, final long periodOffset, @Nullable final String customSliceSuffix) {
        final String key = String.join(StatConstants.KEY_DELIMITER,
                StatConstants.PREFIX_TASK,
                taskId,
                String.valueOf(periodInMinutes),
                String.valueOf(periodOffset));
        return StringUtils.isBlank(customSliceSuffix) ? key : String.join(StatConstants.BUCKET_BIZ_DELIMITER, key, customSliceSuffix);
    }

    public String joinBucketSuffixKeys(@Nullable final String... keys) {
        return Arrays.stream(Optional.ofNullable(keys).orElse(new String[0]))
                .map(v -> StringUtils.defaultIfBlank(v, StatConstants.NULL_MEMBER_KEY))
                .collect(Collectors.joining("|"));
    }

    public long periodOffset(final int periodInMinutes, final LocalDateTime baseTime) {
        return Duration.between(StatConstants.START_DATE_TIME, baseTime).toMinutes() / periodInMinutes;
    }

    @Nonnull
    public LocalDateTime getPeriodTimeByBucketKey(@Nonnull final String bucketKey) {
        final String keyWithoutBizSuffix = Arrays.stream(StringUtils.split(bucketKey, StatConstants.BUCKET_BIZ_DELIMITER))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("非法bucketKey:" + bucketKey));

        final String[] array = StringUtils.split(keyWithoutBizSuffix, StatConstants.KEY_DELIMITER);
        final long periodInMinutes = Long.parseLong(array[array.length - 2]);
        final long periodOffset = Long.parseLong(array[array.length - 1]);
        return StatConstants.START_DATE_TIME.plusMinutes(periodInMinutes * periodOffset);
    }

    public long twoDaysLaterRandomMidnightTimeMillisToNow() {
        final long start = System.currentTimeMillis();
        LocalDateTime after = LocalDateTime.now().plusDays(2);
        after = after.withHour(ThreadLocalRandom.current().nextInt(1, 7));
        after = after.withMinute(ThreadLocalRandom.current().nextInt(0, 60));
        after = after.withSecond(ThreadLocalRandom.current().nextInt(0, 60));
        after = after.with(ChronoField.MILLI_OF_SECOND, ThreadLocalRandom.current().nextInt(0, 1000));
        return DateTimeUtils.toDate(after).getTime() - start;
    }

    @SuppressWarnings("unchecked")
    public List<String> ifKeysExist(final Collection<String> keys) {
        final List<String> keysUse = CollectionUtils.emptyIfNull(keys).stream().filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        if (keysUse.isEmpty()) {
            return Collections.emptyList();
        }
        return (List<String>) RedisUtils.execute(RedisScripts.existKeysScript(), new StringRedisSerializer(), keysUse);
    }

    @SuppressWarnings("unused")
    public String shopIdAsNumber(final String shopId) {
        if (StringUtils.isBlank(shopId)) {
            return shopId;
        }
        if (StringUtils.startsWith(shopId, "A") && StringUtils.length(shopId) > 1) {
            final String withoutPrefix = StringUtils.substring(shopId, 1);
            if (NumberUtils.isParsable(withoutPrefix)) {
                return withoutPrefix;
            }
        }
        return shopId;
    }

    @SuppressWarnings("unused")
    public String skuIdAsNumber(final String skuId) {
        if (StringUtils.isBlank(skuId)) {
            return skuId;
        }
        if (StringUtils.startsWith(skuId, "g") && StringUtils.length(skuId) > 1) {
            final String withoutPrefix = StringUtils.substring(skuId, 1);
            if (NumberUtils.isParsable(withoutPrefix)) {
                return withoutPrefix;
            }
        }
        return skuId;
    }
}
