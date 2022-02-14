package com.zzc.micro.stat.core.supports.scripts;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.ResourceUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
@Configuration
public class RedisScripts {

    private static final RedisScript<Boolean> sumScript;
    private static final RedisScript<Boolean> distinctCountScript;
    private static final RedisScript<Boolean> addSetScript;
    private static final RedisScript<Boolean> addHashScript;
    @SuppressWarnings("rawtypes")
    private static final RedisScript<List> existKeysScript;

    static {
        sumScript = buildRedisScript("scripts/sum.lua", Boolean.class);
        distinctCountScript = buildRedisScript("scripts/distinctCount.lua", Boolean.class);
        addSetScript = buildRedisScript("scripts/addSet.lua", Boolean.class);
        addHashScript = buildRedisScript("scripts/addHash.lua", Boolean.class);
        existKeysScript = buildRedisScript("scripts/existKeys.lua", List.class);
    }

    public static RedisScript<Boolean> sumScript() {
        return sumScript;
    }

    public static RedisScript<Boolean> distinctCountScript() {
        return distinctCountScript;
    }

    public static RedisScript<Boolean> addSetScript() {
        return addSetScript;
    }

    public static RedisScript<Boolean> addHashScript() {
        return addHashScript;
    }

    @SuppressWarnings("rawtypes")
    public static RedisScript<List> existKeysScript() {
        return existKeysScript;
    }

    @SuppressWarnings("SameParameterValue")
    public static <T> RedisScript<T> buildRedisScript(@Nonnull final String scriptName, @Nonnull final Class<T> clazz) {
        final byte[] bytes;
        try {
            bytes = loadScriptBytes(scriptName);
        } catch (IOException e) {
            throw new RedisScriptNotFoundException(scriptName);
        }

        final DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ByteArrayResource(bytes)));
        redisScript.setResultType(clazz);
        return redisScript;
    }

    public static byte[] loadScriptBytes(final String fileName) throws IOException {
        final InputStream inputStream;
        final File file = ResourceUtils.getFile(fileName);
        if (!file.exists()) {
            final ClassPathResource classPathResource = new ClassPathResource(fileName);
            inputStream = classPathResource.getInputStream();
        } else {
            inputStream = new FileInputStream(file);
        }
        return IOUtils.toByteArray(inputStream);
    }
}
