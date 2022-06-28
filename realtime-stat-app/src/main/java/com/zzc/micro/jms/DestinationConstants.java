package com.zzc.micro.jms;

/**
 * <p>Filename: com.zzc.micro.jms.DestinationConstants.java</p>
 * <p>Date: 2022-06-27, 周一, 19:41:53.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
public interface DestinationConstants {

    /**
     * 用户信息变更
     */
    String USER_CHANGE_TOPIC = "t.usercenter.userChange";

    /**
     * 店铺信息变更消息
     */
    String SHOP_CHANGE_TOPIC = "t.usercenter.shopChange";

    /**
     * 订单状态变更
     */
    String TRADE_OPERATE = "t.yxtc.TradeOperate";

    /**
     * 市场店铺关系变更
     */
    String B2B_REL_CHANGE_TOPIC = "t.usercenter.b2bRelChange";


}
