package com.zzc.micro.stat.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付类型
 * @author Administrator
 */
public enum PayType {

    OBP("OBP", "预存款支付"),

    OLP("OLP", "在线支付"),

    GRP("GRP", "货到付款"),

    CRP("CRP", "额度支付"),

    BTP("BTP", "线下转账"),

    CAP("CAP", "现金支付"),

    CPP("CPP", "店铺余额"),

    TYP("TYP", "记账"),

    //散客代客下单新增
    OFC("OFC","线下收银"),

    POS("POS","POS支付"),

    //新加拉卡拉提供的汇款支付方式，区别BTP
    RMP("RMP","汇款支付"),

    WBP("WBP","拉卡拉订货贷"),

    IOU("IOU","白条支付");

    PayType(String payTypeId, String payTypeName){
        this.payTypeId = payTypeId;
        this.payTypeName = payTypeName;
    }

    private String payTypeId;

    private String payTypeName;

    private static Map<String, PayType> payTypeMap = new HashMap<>();

    static {
        payTypeMap.put(OBP.getPayTypeId(), OBP);
        payTypeMap.put(OLP.getPayTypeId(), OLP);
        payTypeMap.put(GRP.getPayTypeId(), GRP);
        payTypeMap.put(CRP.getPayTypeId(), CRP);
        payTypeMap.put(CAP.getPayTypeId(), CAP);
        payTypeMap.put(BTP.getPayTypeId(), BTP);
        payTypeMap.put(CPP.getPayTypeId(), CPP);
        payTypeMap.put(TYP.getPayTypeId(), TYP);
        payTypeMap.put(OFC.getPayTypeId(), OFC);
        payTypeMap.put(POS.getPayTypeId(), POS);
        payTypeMap.put(RMP.getPayTypeId(), RMP);
        payTypeMap.put(WBP.getPayTypeId(), WBP);
        payTypeMap.put(IOU.getPayTypeId(), IOU);
    }

    /**
     * 是否为实时支付方式
     * @param payTypeId
     * @return
     */
    public static boolean isRealTimePay(String payTypeId){
        return Arrays.asList(OBP,CRP,TYP).contains(forValue(payTypeId));
    }

    /**
     * 是否为线下支付方式
     * @param payTypeId
     * @return
     */
    public static boolean isOfflinePay(String payTypeId){
        return Arrays.asList(OLP, GRP, CAP, BTP, CPP, OFC, IOU).contains(forValue(payTypeId));
    }

    public static boolean isGRPCAP(String payTypeId){
        return Arrays.asList(GRP, CAP).contains(forValue(payTypeId));
    }

    public static boolean isRMP(String payTypeId) {
        return RMP.equals(forValue(payTypeId));
    }

    public static boolean isIOU(String payTypeId) {
        return IOU.equals(forValue(payTypeId));
    }

    /**
     * 是否支持原路退款
     */
    public static boolean supportOriginalRefund(String payTypeId) {
        return OLP.getPayTypeId().equals(payTypeId);
    }

    /**
     * 是否支持售中仅退货不退款
     */
    public static boolean supportOnlyReturn(String payTypeId) {
        return GRP.getPayTypeId().equals(payTypeId);
    }

    /**
     * 是否支持收款单
     */
    public static boolean supportReceipt(String payTypeId) {
        return Arrays.asList(OLP, CAP, BTP).contains(forValue(payTypeId));
    }

    /**
     * 是否支持散客
     */
    public static boolean supportFitUser(String payTypeId) {
        return Arrays.asList(OFC, CAP, OLP).contains(forValue(payTypeId));
    }

    @JsonCreator
    public static PayType forValue(String payTypeId){
        return payTypeMap.get(payTypeId);
    }

    @JsonValue
    public String toValue(){
        return this.getPayTypeId();
    }

    public String getPayTypeId() {
        return payTypeId;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public static String getPayTypeNameById(String payTypeId){
        PayType payType = forValue(payTypeId);
        if(payType == null){
            return payTypeId;
        }
        return payType.getPayTypeName();
    }

    /**
     * 车销自定义支付方式
     * 后付
     * @param payTypeId
     * @param tradeType
     * @return
     */
    public static boolean isCustomPayTypeOfTruck(String payTypeId, String tradeType) {
        return StringUtils.equals(GRP.getPayTypeId(), payTypeId)
                && StringUtils.equals(TradeType.TRUCK.getStatusValue(), tradeType);
    }

    /***
     * 自定义支付方式
     * 线下收银
     * @param payTypeId
     * @return
     */
    public static boolean isCustomPayType(String payTypeId) {
        return StringUtils.equals(OFC.getPayTypeId(), payTypeId);
    }

    /**
     * 需要自主结算的支付方式 = 千米账务代收款的支付方式 = 线上支付方式
     * 此类支付方式需要自主结算
     * @note 货到付款比较特殊,相当于一个预支付方式，实际支付方式需要付款后读取 payInfo.acctPayTypeId字段
     * @param payTypeId
     * @return
     */
    public static boolean isSelfSettleBeforePay(String payTypeId) {
        return StringUtils.equals(OLP.getPayTypeId(), payTypeId)
                || StringUtils.equals(RMP.getPayTypeId(), payTypeId)
                || StringUtils.equals(POS.getPayTypeId(), payTypeId)
                || StringUtils.equals(GRP.getPayTypeId(), payTypeId);
    }


    /**
     * 需要自主结算的支付方式 = 千米账务代收款的支付方式 = 线上支付方式
     * 此类支付方式需要自主结算
     * @note 货到付款比较特殊,相当于一个预支付方式，实际支付方式需要付款后读取 payInfo.acctPayTypeId字段
     * @param payTypeId
     * @return
     */
    public static boolean isSelfSettleAfterPay(String payTypeId) {
        return StringUtils.equals(OLP.getPayTypeId(), payTypeId)
                || StringUtils.equals(RMP.getPayTypeId(), payTypeId)
                || StringUtils.equals(POS.getPayTypeId(), payTypeId);
    }

    /**
     * 线上支付
     * @param payTypeId
     * @return
     */
    public static boolean isPayOnline(String payTypeId) {
        return Arrays.asList(OLP, POS, RMP, WBP).contains(forValue(payTypeId));
    }

    /**
     * 线上结算
     * @param payTypeId
     * @return
     * @Desc  理论上线上支付就应该支持线上结算,但是账务奇葩的支付方式太多,此处解耦处理
     */
    public static boolean isSettleOnline(String payTypeId) {
        return Arrays.asList(OLP, POS, RMP, WBP).contains(forValue(payTypeId));
    }

    /**
     * 线上退款
     * @param payTypeId
     * @return
     * @Desc  理论上线上支付就应该支持线上原路退款，但是有POS 和 RMP两个奇葩半成品
     */
    public static boolean isRefundOnline(String payTypeId) {
       return isPayOnline(payTypeId)
               && !StringUtils.equals(POS.getPayTypeId(), payTypeId)
               && !StringUtils.equals(RMP.getPayTypeId(), payTypeId);
    }
}
