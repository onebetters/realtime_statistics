package com.zzc.micro.stat.core.query;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zzc.micro.stat.StatEntry;
import com.zzc.micro.stat.StatQueryRequest;
import com.zzc.micro.stat.StatQueryResult;
import com.zzc.micro.stat.StatTaskIds;
import com.zzc.micro.stat.core.config.metadata.MeasureMetadata;
import com.zzc.micro.stat.core.config.metadata.MeasureMetadataManager;
import com.zzc.micro.stat.core.supports.StatConstants;
import com.zzc.micro.stat.core.supports.StatHelpers;
import com.zzc.micro.stat.utils.DateTimeUtils;
import com.zzc.micro.stat.utils.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatQueryService {

    private final MeasureMetadataManager measureMetadataManager;

    private final Cache<String, List<String>> keysCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .initialCapacity(1)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    @Nonnull
    public List<StatQueryResult> query(@Nonnull StatQueryRequest query) {
        final List<MeasureMetadata> measures = query.getIds()
                                                    .stream()
                                                    .map(measureMetadataManager::findByIdOrCode)
                                                    .filter(Optional::isPresent)
                                                    .map(Optional::get)
                                                    .distinct()
                                                    .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(measures)) {
            return Collections.emptyList();
        }

        // 结束时间，不传默认当前时间
        final LocalDateTime endTimeInclusive = Optional.ofNullable(query.getEndTimeInclusive()).map(DateTimeUtils::toLocalDateTime).orElse(LocalDateTime.now());
        // 开始时间，不传默认结算时间的起始时间点
        final LocalDateTime startTimeInclusive = Optional.ofNullable(query.getStartTimeInclusive())
                .map(DateTimeUtils::toLocalDateTime)
                .orElse(DateTimeUtils.atStartOfDay(endTimeInclusive));
        final String mapSuffixKey = StatHelpers.joinBucketSuffixKeys(query.getGroups());
        final long limit = query.getLimit();

        final List<StatQueryResult> results = new ArrayList<>();
        measures.forEach(measure -> {
            final List<String> bucketKeys = this.bucketKeys(measure, startTimeInclusive, endTimeInclusive, mapSuffixKey);
            final List<String> existsBulkKeys = this.taskExistKeys(measure, bucketKeys);
            existsBulkKeys.forEach(bulkKey -> {
                final LocalDateTime   periodTime = StatHelpers.getPeriodTimeByBucketKey(bulkKey);
                final List<StatEntry> entries;
                if (StringUtils.isNotBlank(query.getKey())) {
                    final Number value = RedisUtils.getScoreFromSortedSet(bulkKey, query.getKey());
                    entries = Objects.nonNull(value) ? Collections.singletonList(new StatEntry(this.unWarpKey(query.getKey()),
                            value)) : Collections.emptyList();
                } else {
                    final List<ZSetOperations.TypedTuple<String>> values = limit <= 0 ? Collections.emptyList() : RedisUtils.topNDescFromSortedSet(bulkKey,
                            limit);
                    entries = values.stream()
                            .filter(memberAndScore -> Objects.nonNull(memberAndScore.getScore()))
                            .map(memberAndScore -> new StatEntry(this.unWarpKey(memberAndScore.getValue()), memberAndScore.getScore()))
                            .collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(entries)) {
                    results.add(new StatQueryResult(StatTaskIds.of(measure.getCode()), DateTimeUtils.toDate(periodTime), entries));
                }
            });
        });
        return results;
    }

    private String unWarpKey(final String key) {
        return StringUtils.equals(key, StatConstants.NULL_MEMBER_KEY) ? "" : StringUtils.defaultString(key);
    }

    public List<String> taskExistKeys(final MeasureMetadata measure, final List<String> checkKeys) {
        try {
            return keysCache.get(measure.getId() + "|" + checkKeys.hashCode(), () -> taskExistsKeys(checkKeys));
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }

    public List<String> taskExistsKeys(final List<String> checkKeys) {
        return StatHelpers.ifKeysExist(checkKeys);
    }

    @Nonnull
    private List<String> bucketKeys(@Nonnull final MeasureMetadata measure,
            @Nonnull final LocalDateTime startTimeInclusive,
            @Nonnull final LocalDateTime endTimeInclusive,
            @Nullable final String bucketSliceKey) {
        final int periodInMinutes = measure.getPeriod();
        LocalDateTime loopTime = startTimeInclusive;
        final List<String> keys = new ArrayList<>();
        while (!loopTime.isAfter(endTimeInclusive)) {
            final String key = StatHelpers.taskBucketKey(measure.getId(),
                    measure.getPeriod(),
                    StatHelpers.periodOffset(periodInMinutes, loopTime),
                    bucketSliceKey);
            keys.add(key);
            loopTime = loopTime.plusMinutes(periodInMinutes);
        }
        return keys;
    }
}
