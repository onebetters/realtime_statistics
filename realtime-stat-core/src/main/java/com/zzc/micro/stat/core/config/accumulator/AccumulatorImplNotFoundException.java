package com.zzc.micro.stat.core.config.accumulator;

/**
 * @author Administrator
 */
public class AccumulatorImplNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3599774468959801426L;

    public AccumulatorImplNotFoundException(String name) {
        super("Cannot find aggregator by name: " + name);
    }
}
