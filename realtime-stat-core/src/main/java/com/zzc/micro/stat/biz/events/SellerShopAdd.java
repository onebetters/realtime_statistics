package com.zzc.micro.stat.biz.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SellerShopAdd extends ShopChange {
    private static final long serialVersionUID = -1762483350579143522L;
}
