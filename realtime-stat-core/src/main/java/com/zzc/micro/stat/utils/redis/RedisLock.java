package com.zzc.micro.stat.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Administrator
 */
@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class RedisLock {
    private final static String SCRIPT_LOCK =
            // @formatter:off
                      "local key = KEYS[1];\n"
                    + "local value = ARGV[1];\n"
                    + "local ttlMillis = tonumber(ARGV[2]);\n"
                    + "if ( redis.call('SETNX', key, value) == 1 ) then\n"
                    + "    redis.call('PEXPIRE', key, ttlMillis);\n"
                    + "    return true;\n"
                    + "end;";
            // @formatter:on

    private static final String PREFIX = "pur_micro_service:lock:";
    private final        String key;
    private final        long   timeout;

    public RedisLock(String key, long timeoutSeconds) {
        this(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    public RedisLock(String key, final long timeout, final TimeUnit unit) {
        this.key = PREFIX + key;
        this.timeout = TimeoutUtils.toMillis(timeout, unit);
    }

    public boolean tryLock() {
        return Optional.ofNullable(RedisUtils.execute(new DefaultRedisScript<>(SCRIPT_LOCK, Boolean.class), Collections.singletonList(key), true, timeout)).orElse(false);
    }

    public boolean lockUntilSuccessful(final long waitTimeoutSeconds) {
        return this.lockUntilSuccessful(waitTimeoutSeconds, TimeUnit.SECONDS);
    }

    public boolean lockUntilSuccessful(final long waitTimeout, final TimeUnit unit) {
        final long start = System.currentTimeMillis();
        final long maxWait = unit.toMillis(waitTimeout);
        int tryTime = 0;
        while ((System.currentTimeMillis() - start) < maxWait) {
            if (this.tryLock()) {
                return true;
            }
            try {
                final long sleepTime = Math.min(100 * (1 << tryTime), 5000);
                if ((System.currentTimeMillis() - start + sleepTime) >= maxWait) { // 休眠后超时
                    return false;
                }
                TimeUnit.MILLISECONDS.sleep(sleepTime); // 随机时间防止活锁
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            tryTime++;
        }
        return false;
    }

    public void release() {
        RedisUtils.delete(key);
    }

    public void run(final Runnable action) {
        this.run(action, true);
    }

    /**
     * @param autoRelease 操作完action事件后，是否立即释放锁.
     *                    原则上尽量为true，为false情况需要自行释放锁，或者锁自动失效
     */
    public void run(final Runnable action, final boolean autoRelease) {
        if (this.tryLock()) {
            try {
                action.run();
            } finally {
                if (autoRelease) {
                    this.release();
                }
            }
        }
    }

    public <E extends Throwable> void run(final Runnable action, @Nonnull final Supplier<? extends E> failedExceptionSupplier) throws E {
        if (this.tryLock()) {
            try {
                action.run();
            } finally {
                this.release();
            }
        } else {
            throw failedExceptionSupplier.get();
        }
    }

    public void run(final Runnable action, final String failedExceptionMessage) throws Exception {
        this.run(action, () -> new Exception(failedExceptionMessage));
    }
}
