package com.zzc.micro.stat.order;

/**
 * 订单类型
 * @author Administrator
 */
public enum TradeType {
    /**
     * 没有绑定买家自己商品
     */
    NORMAL("NORMAL","普通订单"),
    TRUCK("TRUCK", "车销订单"),
    /**
     * 绑定买家自己商品
     */
    ONLINE("ONLINE","线上采购"),
    OFFLINE("OFFLINE","线下采购"),
    /**
     * 收银订单
     */
    GROUPON("GROUPON","多人拼团订单");

    private String type;

    private String description;

    TradeType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getStatusValue() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
