package com.zzc.micro.supports.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

/**
 * <p>Filename: com.zzc.micro.supports.configuration.RedisConfiguration.java</p>
 * <p>Date: 2022-06-27, 周一, 19:32:15.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

    @Bean
    public RedisSerializer<?> jacksonSerializer() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new ParameterNamesModule()).registerModule(new JavaTimeModule()).registerModule(new Jdk8Module());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 忽略未知属性。版本升级时，后续版本可能加字段并提前升级Redis缓存
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Bean
    public RedisSerializer<?> stringSerializer() {
        return new StringRedisSerializer();
    }

    @Primary
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(this.jacksonSerializer());
        template.setKeySerializer(this.stringSerializer());
        template.setValueSerializer(this.jacksonSerializer());
        template.setHashKeySerializer(this.stringSerializer());
        template.setHashValueSerializer(this.jacksonSerializer());
        return template;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}