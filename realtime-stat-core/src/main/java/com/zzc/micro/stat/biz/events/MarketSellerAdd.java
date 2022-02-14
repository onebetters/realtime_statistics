package com.zzc.micro.stat.biz.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nonnull;

/**
 * 市场入驻商家
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MarketSellerAdd extends AbstractEvent {
    private static final long serialVersionUID = 6450590285627172375L;

    private String userId;
    private String appId;
    private String b2bGroupId;
    private String marketId;
    private String sellerShopId;
    private String sellerProvince;
    private String sellerCity;

    @Nonnull
    @Override
    public String getIdentifier() {
        return userId;
    }
}
