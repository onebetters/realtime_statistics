package com.zzc.micro.stat.core.collector;

import com.zzc.micro.stat.core.supports.scripts.RedisScripts;
import com.zzc.micro.stat.utils.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * 不存储原始全量数据，只存储相关的业务id串
 *
 * @author Administrator
 */
@Slf4j
@RequiredArgsConstructor
public class RedisQuickCollector extends QuickCollector {

    @Override
    boolean save(String key, String id) {
        return RedisUtils.execute(RedisScripts.addSetScript(), Collections.singletonList(key), id, this.defaultTtlMillis());
    }
}
