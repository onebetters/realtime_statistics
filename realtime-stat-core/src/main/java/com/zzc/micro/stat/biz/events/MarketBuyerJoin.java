package com.zzc.micro.stat.biz.events;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nonnull;

/**
 * 市场买家
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MarketBuyerJoin extends AbstractEvent {
    private static final long serialVersionUID = 6450590285627172375L;

    private String userId;
    private String appId;
    private String b2bGroupId;
    private String marketId;
    private String buyerShopId;
    private String buyerProvince;
    private String buyerCity;

    @Nonnull
    @Override
    public String getIdentifier() {
        return userId;
    }
}
