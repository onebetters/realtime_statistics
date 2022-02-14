package com.zzc.micro.stat.biz.events;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzc.micro.stat.order.*;
import com.zzc.micro.stat.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static com.zzc.micro.stat.order.TradeOperateEvent.*;

/**
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MarketTradeDeal extends AbstractEvent {
    private static final long serialVersionUID = -7692002497386768107L;
    public static final TradeOperateEvent[] SUPPORT_EVENTS = {
            GROUPON_SUCCEED, DELIVER, PART_DELIVED, PAY, PART_PAY
    };

    /**
     * 订单ID
     */
    private String id;
    /**
     * 市场编号
     */
    private String marketId;
    private String marketProvince;
    private String marketCity;
    /**
     * 商家店铺编号
     */
    private String sellerShopId;
    /**
     * 客户编号
     */
    private String buyerUserId;
    /**
     * 客户店铺编号
     */
    private String buyerShopId;
    /**
     * 客户收货地址省
     */
    private String consigneeProvince;
    /**
     * 客户收货地址：市
     */
    private String consigneeCity;
    /**
     * 订单应付金额（不含手续费）
     */
    private BigDecimal totalPrice;
    /**
     * 专属小程序appId
     */
    private String appId;
    /**
     * 集团B2B
     */
    private String b2bGroupId;
    /**
     * 订单类型
     */
    private String tradeType;
    /**
     * 成团状态
     *
     * @see GrouponStatus
     */
    private String grouponStatus;
    /**
     * 成团时间
     */
    private Date grouponTime;
    /**
     * 支付类型标识
     */
    private String payTypeId;
    /**
     *
     */
    private Date deliverTime;
    /**
     * 支付状态
     *
     * @see PayState
     */
    private String payState;
    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * （针对买家）是否首单
     */
    private boolean firstOrder;

    /**
     * 市场客户经理ID
     */
    private String marketSaleManId;

    /**
     * @see #SUPPORT_EVENTS
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    public boolean statFilter(final TradeOperateEvent event) {
        if (Objects.equals(this.getTradeType(), TradeType.GROUPON.getStatusValue())) {
            return Objects.equals(event, TradeOperateEvent.GROUPON_SUCCEED) && Objects.equals(this.getGrouponStatus(), GrouponStatus.GROUPED.getStatusValue());
        } else if (Objects.equals(this.getPayTypeId(), PayType.GRP.getPayTypeId())) {
            return (Objects.equals(event, DELIVER) || Objects.equals(event, PART_DELIVED)) && Objects.nonNull(this.getDeliverTime());
        } else {
            return (Objects.equals(event, PAY) || Objects.equals(event, PART_PAY)) && Objects.nonNull(this.getPayTime());
        }
    }

    @Nonnull
    @Override
    public String getIdentifier() {
        return this.id;
    }

    @Nonnull
    @Override
    public LocalDateTime getBaseTime() {
        if (Objects.equals(this.getTradeType(), TradeType.GROUPON.getStatusValue())) {
            return DateTimeUtils.toLocalDateTime(this.getGrouponTime());
        } else if (Objects.equals(this.getPayTypeId(), PayType.GRP.getPayTypeId())) {
            return DateTimeUtils.toLocalDateTime(this.getDeliverTime());
        } else {
            return DateTimeUtils.toLocalDateTime(this.getPayTime());
        }
    }
}
