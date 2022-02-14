package com.zzc.micro.stat.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付状态
 * @author Administrator
 */
public enum PayState {

    NOT_PAID("NOT_PAID", "未支付"),

    PARTIAL_PAID("PARTIAL_PAID", "部分支付"),

    PAID("PAID", "已支付"), //目前没有这个状态

    PARTIAL_REFUND("PARTIAL_REFUND", "部分退款"),

    REFUND("REFUND", "已退款"),

    REFUNDING("REFUNDING", "退款中"),

    REFUND_FAILED("REFUND_FAILED", "退款失败");

    PayState(String stateId, String description) {
        this.stateId = stateId;
        this.description = description;
    }

    private final String stateId;

    private final String description;

    private static final Map<String, PayState> payStateMap = new HashMap<>();

    static {
        payStateMap.put(NOT_PAID.getStateId(), NOT_PAID);
        payStateMap.put(PARTIAL_PAID.getStateId(), PARTIAL_PAID);
        payStateMap.put(PAID.getStateId(), PAID);
        payStateMap.put(PARTIAL_REFUND.getStateId(), PARTIAL_REFUND);
        payStateMap.put(REFUND.getStateId(), REFUND);
        payStateMap.put(REFUNDING.getStateId(), REFUNDING);
        payStateMap.put(REFUND_FAILED.getStateId(), REFUND_FAILED);
    }

    @JsonCreator
    public static PayState forValue(String stateId) {
        return payStateMap.get(stateId);
    }

    @JsonValue
    public String toValue() {
        return this.getStateId();
    }

    public String getStateId() {
        return stateId;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 是否需要支付
     * @param payState
     * @return
     */
    public static boolean needPay(PayState payState) {
        if (PayState.NOT_PAID.equals(payState) || PayState.PARTIAL_PAID.equals(payState)) {
            return true;
        }

        return false;
    }
}
