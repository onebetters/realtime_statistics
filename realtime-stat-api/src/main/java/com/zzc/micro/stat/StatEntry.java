package com.zzc.micro.stat;

import lombok.Data;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class StatEntry implements Serializable {
    private static final long serialVersionUID = 3039647476067145931L;

    private String key;
    private Number value;

    public StatEntry(@Nonnull String key, @Nonnull Number value) {
        this.key = key;
        this.value = value;
    }
}
