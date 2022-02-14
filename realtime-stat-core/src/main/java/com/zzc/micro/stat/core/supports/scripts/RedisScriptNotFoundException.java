package com.zzc.micro.stat.core.supports.scripts;

/**
 * @author Administrator
 */
public class RedisScriptNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8157904255248498840L;

    public RedisScriptNotFoundException(String script) {
        super("script can not be found: " + script);
    }
}
