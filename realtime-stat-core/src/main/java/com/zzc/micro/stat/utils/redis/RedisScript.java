package com.zzc.micro.stat.utils.redis;

/**
 * @author Administrator
 */
public interface RedisScript {

    // @formatter:off
    String BATCH_ADD_SET = "" +
            "local totalNumber = table.getn(ARGV)\n" +
            "local setKey = KEYS[1]\n" +
            "local expireTime = tonumber(KEYS[2])\n" +
            "local existKey = redis.call(\"EXISTS\", setKey) > 0\n" +
            "if totalNumber < 1 then\n" +
            "    return false\n" +
            "end\n" +
            "for i = 1, totalNumber do\n" +
            "    local value = ARGV[i]\n" +
            "    if value then\n" +
            "        redis.call(\"SADD\", setKey, value)\n" +
            "    end\n" +
            "end\n" +
            "if not existKey then\n" +
            "    redis.call(\"EXPIRE\", setKey, expireTime)\n" +
            "end\n" +
            "return true\n";


}
