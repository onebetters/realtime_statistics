package com.zzc.micro.stat.biz.events;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MarketAdd extends ShopChange {
    private static final long serialVersionUID = -1762483350579143522L;

    private String appId;

    private String b2bGroupId;
}
