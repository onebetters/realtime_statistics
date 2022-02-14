package com.zzc.micro.stat.core.config.event;

/**
 * @author Administrator
 */
public class EventNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 6072720932658234798L;

    public EventNotFoundException(String name) {
        super("Can not find event by name: " + name);
    }
}
