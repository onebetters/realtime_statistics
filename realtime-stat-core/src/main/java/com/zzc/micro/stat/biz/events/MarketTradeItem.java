package com.zzc.micro.stat.biz.events;

import com.zzc.micro.stat.order.GrouponStatus;
import com.zzc.micro.stat.order.PayState;
import com.zzc.micro.stat.order.PayType;
import com.zzc.micro.stat.order.TradeType;
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
import java.util.List;
import java.util.Objects;

/**
 * 市场商品
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MarketTradeItem extends AbstractEvent {
    private static final long serialVersionUID = 1669656895459449381L;

    /**
     * OID
     */
    private String tradeOrderId;
    /**
     * 订单ID
     */
    private String tradeId;
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
     * 发货时间
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
     * 订单商品平铺开
     */
    private String spuId;
    private String skuId;
    /**
     * 品牌Id
     */
    private String brandId;
    /**
     * 类目Id
     */
    private List<String> catIds;
    /**
     * 市场A编号&类目Id  例如：A7081184&1001
     * 场景：GM查看所有市场热销商品分类，统计热销类目，改善经营方向
     */
    private List<String> marketIdAndCatIdList;
    /**
     * 购买数量
     */
    private BigDecimal count;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 商品总价（price * count），统计金额用这个，和非实时指标保持一致
     * price().multiply(count).setScale(2, BigDecimal.ROUND_DOWN)
     */
    private BigDecimal itemTotalPrice;

    @Nonnull
    @Override
    public String getIdentifier() {
        return String.join("|", tradeId, tradeOrderId);
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
