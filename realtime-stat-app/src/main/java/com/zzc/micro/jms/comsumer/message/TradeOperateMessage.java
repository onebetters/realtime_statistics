package com.zzc.micro.jms.comsumer.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Filename: com.zzc.micro.jms.comsumer.message.TradeOperateMessage.java</p>
 * <p>Date: 2022-06-27, 周一, 19:44:51.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeOperateMessage {
    /**
     * 订单号
     */
    @JSONField(name = "tid")
    @JsonProperty("tid")
    private String tradeId;
    /**
     * 卖家编号
     */
    @JSONField(name = "sellerUserCode")
    @JsonProperty("sellerUserCode")
    private String sellerShopId;
    /**
     * 买家编号
     */
    @JSONField(name = "buyerUserCode")
    @JsonProperty("buyerUserCode")
    private String buyerUserId;
    /**
     * 订单事件ID
     */
    private int eventId;
    /**
     * 事件编码
     */
    private String eventName;
    /**
     * 订单类型
     */
    private String tradeType;
    /**
     * 卖家用户类型
     */
    private Integer sellerUserType;
    /**
     * 买家用户类型
     */
    private Integer buyerUserType;
    /**
     * 售后退货单号
     */
    @JSONField(name = "applyId")
    @JsonProperty("applyId")
    private String returnApplyId;
}
