package com.zzc.micro.jms.comsumer;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.Duration;
import java.time.Instant;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>Filename: com.zzc.micro.jms.comsumer.AbstractJsonMessageConsumer.java</p>
 * <p>Date: 2022-06-27, 周一, 19:42:09.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
abstract class AbstractJsonMessageConsumer {
    private static final long SLOW_WARN_MILLIS = 3_000L;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    abstract String getMessageFlag();

    private <T> void onMessage(final Message source,
            final Function<String, T> messageConverter,
            final Consumer<T> consumer) {
        final Instant start = Instant.now();
        final String messageFlag = getMessageFlag();
        MDC.put("messageFlag", messageFlag);
        log.info("监听到{}: {}", messageFlag, source);
        T message;
        try {
            final String json = this.parseMessageBody(source);
            log.info("{}的消息内容:{}", messageFlag, json);
            message = messageConverter.apply(json);
            consumer.accept(message);
            log.info("{}处理结束,message={}", messageFlag, message);
        } catch (Exception e) {
            log.error("{}处理失败", messageFlag, e);
        } finally {
            final long costTime = Duration.between(start, Instant.now()).toMillis();
            if (costTime > SLOW_WARN_MILLIS) {
                log.info("警告:处理{}耗时超过{}毫秒,message={}", messageFlag, SLOW_WARN_MILLIS, source);
            }
        }
    }

    <T> void onMessage(final Message source, final Class<T> clazz, final Consumer<T> consumer) {
        this.onMessage(source, msg -> JSON.parseObject(msg, clazz), consumer);
    }

    private String parseMessageBody(final Message source) throws JMSException {
        if (source instanceof TextMessage) {
            return ((TextMessage) source).getText();
        } else if (source instanceof MapMessage) {
            final MapMessage mapMessage = (MapMessage) source;
            final Enumeration<?> enumeration = mapMessage.getMapNames();
            final Map<String, Object> map = new HashMap<>();
            while (enumeration.hasMoreElements()) {
                Object k = enumeration.nextElement();
                String key = k instanceof String ? (String) k : k.toString();
                Object value = mapMessage.getObject(key);
                map.put(key, value);
            }
            return JSON.toJSONString(map);
        }
        throw new IllegalArgumentException("不支持的Message类型，目前只支持TextMessage,MapMessage");
    }
}
