package com.zzc.micro.stat.order;

/**
 * 订单中心TradeEvent
 * @author Administrator
 */
public enum TradeOperateEvent {
    CREATE(0, "下单", "创建订单"),

    MODIFY_CASH(1, "订单改价", "修改订单价格"),

    PAY(2, "付款", "订单付款"),

    REFUND(3, "订单退款", "卖家退款给买家"),

    DELIVER(4, "订单发货", "订单已发货"),

    RETURN(5, "订单退货", "订单已退货"),

    CANCEL(6, "取消订单", "订单已被取消"),

    CONFIRM(7, "确认收货", "买家签收或确认收货"),

    COMPLETE(8, "完成", "订单已完成"),

    APPLY_REFUND(9, "申请退款", "买家申请退款"),

    APPLY_RETURN(10, "申请退货", "买家申请退货"),

    APPLY_PASS(11, "审核通过申请", "申请已审核通过"),

    APPLY_REJECT(12, "拒绝申请", "申请已拒绝"),

    APPLY_CANCEL(13, "取消申请", "申请已取消"),

    DISPATCH(14, "卖家甩单", "卖家向下级网点分配订单"),

    DISPATCH_CANCEL(15, "取消甩单", "卖家取消分配订单"),

    /**
     * 以下为订单审核流程新加的操作事件
     */
    MODIFY(16, "修改订单", "卖家修改订单"),

    AUDIT_TRADE(17, "订单审核", "该订单通过审核"),

    AUDIT_FINANCE(18, "财务审核", "该订单已通过财务审核"),

    PACK(19, "出库审核", "该订单已通过出库审核"),

    BACK(20, "退回重审", "审核未通过，订单退回重审"),

    PART_PAY(21, "部分支付", "订单部分付款"),

    PART_PACK(22, "部分出库", "订单部分出库"),

    PART_DELIVED(23, "部分发货", "订单部分发货"),

    /*PART_RETURNED(24,"部分退货","订单部分退货"),

    PART_REFUNDED(25,"部分退款","订单部分退款"),*/

    CANCEL_PACK(26, "取消出库", "取消出库并作废包裹单"),

    CANCEL_DELIVE(27, "取消发货", "取消发货"),

    INVOICE(29, "订单开发票", "给买家开发票"),

    DISPATCH_ASSIGN(30, "分配甩单", "卖家将订单分配给指定网点"),

    DISPATCH_ACCEPT(31, "接受甩单", "网点接受分配的订单"),

    DISPATCH_REJECT(32, "拒绝甩单", "网点拒绝分配的订单"),

    DISPATCH_DELIVER(33, "甩单发货", "网点已发货分配的订单"),

    APPLY_PAY(34, "申请支付", "申请支付"),

    SETTLE(35, "结算", "订单结算"),

    GROUPON_SUCCEED(36, "拼团成功", "(拼团订单)拼团成功"),

    APPLY_COMPLETE(37, "退货退款单完成", "退货退款单完成"),

    APPLY_DELIVERED(38, "退货退款单买家已发货", "退货单或者退货退款单，商家同意退货退款后，买家操作发货或者商家安排配送员上门取件"),

    APPLY_RECEIVED(39, "退货退款单卖家已签收", "退货单或者退货退款单，卖家收到货并在系统上确认收货"),

    SUB_PAY(40, "补款", "订单补款");

    private final int id;
    private final String event;
    private final String detail;

    TradeOperateEvent(int id, String event, String detail) {
        this.id = id;
        this.event = event;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    public String getDetail() {
        return detail;
    }
}
