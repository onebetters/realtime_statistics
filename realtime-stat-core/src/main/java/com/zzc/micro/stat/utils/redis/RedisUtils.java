package com.zzc.micro.stat.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    public void setRedisTemplate(final RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    @SuppressWarnings({/*"rawtypes", */"unchecked"})
    public static <T> T get(final String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param timeout 超时时间 - 单位：秒
     */
    public static void set(final String key, final Object value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    public static void set(final String key, final Object value, long timeout, final TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public static void delete(final String key) {
        redisTemplate.delete(key);
    }

    public static Boolean setIfAbsent(final String key, final Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public static boolean hasKey(final String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
    }

    public static Boolean expire(final String key, final long timeout) {
        return RedisUtils.redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return RedisUtils.redisTemplate.expire(key, timeout, unit);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> getSet(final String key) {
        final Set<Object> members = redisTemplate.opsForSet().members(key);
        if (Objects.isNull(members)) {
            return Collections.emptySet();
        } else {
            return (Set<T>) members;
        }
    }

    public static Long ttl(final String key, final TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    public static <T> long addSet(final String key, final T member) {
        return Optional.ofNullable(redisTemplate.opsForSet().add(key, member)).orElse(0L);
    }

    public static <T> long addSet(final String key, final Collection<T> members) {
        if (CollectionUtils.isNotEmpty(members)) {
            return Optional.ofNullable(redisTemplate.opsForSet().add(key, members.toArray(new Object[0]))).orElse(0L);
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<ZSetOperations.TypedTuple<T>> getSetWithScore(final String key) {
        final Set<?> items = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        return Optional.ofNullable(items).map(s -> (Set<ZSetOperations.TypedTuple<T>>) s).orElse(Collections.emptySet());
    }

    public static <T> void addSetWithScore(final String key, final T value, final double score) {
        final Set<?> items = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 添加到队列
     */
    public static <T> void addQueue(final String key, final Collection<T> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            redisTemplate.opsForList().leftPushAll(key, values.toArray());
        }
    }

    /**
     * 获取并删除一个队列元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T pollQueue(final String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取队列信息
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getQueue(final String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 查看队列大小
     */
    public static long sizeQueue(final String key) {
        return Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
    }

    /**
     * 添加元素到延时队列
     */
    public static <T> void addToDelayQueue(final String key, final T value, final long timeout, final TimeUnit unit) {
        if (Objects.nonNull(value)) {
            addToDelayQueue(key, Collections.singletonList(value), timeout, unit);
        }
    }

    /**
     * 添加元素到延时队列
     */
    public static <T> void addToDelayQueue(final String key, final Collection<T> values, final long timeout, final TimeUnit unit) {
        if (CollectionUtils.isNotEmpty(values)) {
            final double expireTimeMillis = (double) (System.currentTimeMillis() + TimeoutUtils.toMillis(timeout, unit));
            redisTemplate.opsForZSet().add(key, values.stream().map(v -> new DefaultTypedTuple<Object>(v, expireTimeMillis)).collect(Collectors.toSet()));
        }
    }

    /**
     * 获取并删除一个延时队列元素
     */
    @SuppressWarnings("unchecked")
    public static <T> T pollFromDelayQueue(final String key) {
        final double max = System.currentTimeMillis();
        final Set<T> items = (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, -1, max, 0, 1);
        final T value = Optional.ofNullable(items).orElse(Collections.emptySet()).stream().findFirst().orElse(null);
        if (Objects.nonNull(value)) {
            redisTemplate.opsForZSet().remove(key, value);
        }
        return value;
    }

    /**
     * 获取并删除一个延时队列元素
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<ZSetOperations.TypedTuple<T>> pollAllFromDelayQueue(final String key) {
        final double max = System.currentTimeMillis();
        final Set<?> items = redisTemplate.opsForZSet().rangeByScoreWithScores(key, -1, max, 0, Long.MAX_VALUE);
        redisTemplate.opsForZSet().removeRangeByScore(key, -1, max);
        return Optional.ofNullable(items).map(s -> (Set<ZSetOperations.TypedTuple<T>>) s).orElse(Collections.emptySet());
    }

    @Nullable
    public static <T, K> Double getScoreFromSortedSet(final String key, final K member) {
        return redisTemplate.opsForZSet().score(key, member);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T, K> List<ZSetOperations.TypedTuple<K>> topNDescFromSortedSet(final String key, final long limit) {
        final Set<ZSetOperations.TypedTuple<Object>> membersWithScores = redisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, Double.MIN_VALUE, Double.MAX_VALUE, 0, limit);
        return Optional.ofNullable(membersWithScores)
                .orElse(Collections.emptySet())
                .stream()
                .map(m -> (ZSetOperations.TypedTuple<K>) m)
                .collect(Collectors.toList());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T, K> List<ZSetOperations.TypedTuple<K>> topNAscFromSortedSet(final String key, final long limit) {
        final Set<ZSetOperations.TypedTuple<Object>> membersWithScores = redisTemplate.opsForZSet()
                .rangeByScoreWithScores(key, Double.MIN_VALUE, Double.MAX_VALUE, 0, limit);
        return Optional.ofNullable(membersWithScores)
                .orElse(Collections.emptySet())
                .stream()
                .map(m -> (ZSetOperations.TypedTuple<K>) m)
                .collect(Collectors.toList());
    }

    public static <HK, HV> HV hashGet(final String hashKey, final HK hashField) {
        HashOperations<String, HK, HV> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(hashKey, hashField);
    }

    public static <HK, HV> void hashPut(final String hashKey, final HK hashField, final HV hashValue) {
        HashOperations<String, HK, HV> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(hashKey, hashField, hashValue);
    }

    public static <HK, HV> void hashDel(final String hashKey, final HK hashField) {
        HashOperations<String, HK, HV> opsForHash = redisTemplate.opsForHash();
        opsForHash.delete(hashKey, hashField);
    }

    public static <HK, HV> void hashPutAll(final String hashKey, final Map<HK, HV> hashMap) {
        HashOperations<String, HK, HV> opsForHash = redisTemplate.opsForHash();
        opsForHash.putAll(hashKey, hashMap);
    }

    public static <HK, HV> Map<HK, HV> hashEntries(final String hashKey) {
        HashOperations<String, HK, HV> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(hashKey);
    }

    public static void hashIncrement(final String hashKey, final String hashField, long increment) {
        redisTemplate.opsForHash().increment(hashKey, hashField, increment);
    }

    /**
     * 执行自定义脚本
     */
    public static <T> T execute(final RedisScript<T> script, final List<String> keys, final Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

    /**
     * 执行自定义脚本
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T execute(final RedisScript<T> script, RedisSerializer resultSerializer, final List<String> keys, final Object... args) {
        return (T) redisTemplate.execute(script, redisTemplate.getKeySerializer(), resultSerializer, keys, args);
    }

    /**
     * 执行管道
     */
    public static void executePipeline(BiConsumer<RedisConnection, RedisTemplate<?, ?>> command) {
        redisTemplate.executePipelined((RedisCallback<Long>) redisConnection -> {
            command.accept(redisConnection, redisTemplate);
            return null;
        });
    }

    @Nonnull
    public static Collection<String> scan(final String pattern) {
        return scan(pattern, -1);
    }

    @Nonnull
    public static Collection<String> scan(final String pattern, final long limit) {
        @SuppressWarnings("unchecked") final RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        final ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
        try (final Cursor<String> cursor = redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(connection.scan(options),
                keySerializer::deserialize))) {
            assert cursor != null;
            return IteratorUtils.toList(cursor).stream().distinct().collect(Collectors.toList());
        } catch (IOException e) {
            log.error("close cursor error", e);
            return Collections.emptyList();
        }
    }

    public static Long increase(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

}
