package com.zzc.micro.stat.biz.events;

import com.alibaba.fastjson.JSON;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEvent implements Event, Serializable {
    private static final long serialVersionUID = 5245147150686405792L;

    @Builder.Default
    private LocalDateTime baseTime = LocalDateTime.now();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
