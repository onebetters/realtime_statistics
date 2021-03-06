package com.zzc.micro.stat;

/**
 * Auto generated by StatTaskIdGenerator
 */
@SuppressWarnings("unused")
public enum StatTaskIds {
    BUYER_INCR_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时新增客户数"),

    BUYER_INCR_PER_DAY_4_APP("b2bGroupId", "小程序，每天新增客户数"),

    BUYER_INCR_PROVINCE_PER_DAY_4_APP("b2bGroupId,buyerProvince", "小程序，按省分组，每天新增客户数"),

    BUYER_INCR_CITY_PER_DAY_4_APP("b2bGroupId,buyerCity", "小程序，按市分组，每天新增客户数"),

    BUYER_INCR_PER_HOUR_4_QM("", "千米，每小时新增客户数"),

    BUYER_INCR_PER_DAY_4_QM("", "千米，每天新增客户数"),

    BUYER_INCR_PROVINCE_PER_DAY_4_QM("buyerProvince", "千米，按省分组，每天新增客户数"),

    BUYER_INCR_CITY_PER_DAY_4_QM("buyerCity", "千米，按市分组，每天新增客户数"),

    BUYER_INCR_BY_MARKET_ID_PER_HOUR("marketId", "市场，每小时新增客户数"),

    BUYER_INCR_BY_MARKET_ID_PER_DAY("marketId", "市场，每天新增客户数"),

    MARKET_INCR_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时新增市场数"),

    MARKET_INCR_PER_DAY_4_APP("b2bGroupId", "小程序，每天新增市场数"),

    MARKET_INCR_PROVINCE_PER_DAY_4_APP("b2bGroupId,province", "小程序，按省分组，每天新增市场数"),

    MARKET_INCR_CITY_PER_DAY_4_APP("b2bGroupId,city", "小程序，按市分组，每天新增市场数"),

    MARKET_INCR_PER_HOUR_4_QM("", "千米，每小时新增市场数"),

    MARKET_INCR_PER_DAY_4_QM("", "千米，每天新增市场数"),

    MARKET_INCR_PROVINCE_PER_DAY_4_QM("province", "千米，按省分组，每天新增市场数"),

    MARKET_INCR_CITY_PER_DAY_4_QM("city", "千米，按市分组，每天新增市场数"),

    SELLER_INCR_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时新增商家数"),

    SELLER_INCR_PER_DAY_4_APP("b2bGroupId", "小程序，每天新增商家数"),

    SELLER_INCR_PROVINCE_PER_DAY_4_APP("b2bGroupId,sellerProvince", "小程序，按省分组，每天新增商家数"),

    SELLER_INCR_CITY_PER_DAY_4_APP("b2bGroupId,sellerCity", "小程序，按市分组，每天新增商家数"),

    SELLER_INCR_PER_HOUR_4_QM("", "千米，每小时新增商家数"),

    SELLER_INCR_PER_DAY_4_QM("", "千米，每天新增商家数"),

    SELLER_INCR_PROVINCE_PER_DAY_4_QM("sellerProvince", "千米，按省分组，每天新增商家数"),

    SELLER_INCR_CITY_PER_DAY_4_QM("sellerCity", "千米，按市分组，每天新增商家数"),

    SELLER_INCR_BY_MARKET_ID_PER_HOUR("marketId", "市场，每天新增商家数"),

    SELLER_INCR_BY_MARKET_ID_PER_DAY("marketId", "市场，每天新增商家数"),

    TRADE_AMOUNT_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时订单交易额"),

    TRADE_AMOUNT_PER_DAY_4_APP("b2bGroupId", "小程序，每天订单交易额"),

    TRADE_AMOUNT_PROVINCE_PER_DAY_4_APP("b2bGroupId,marketProvince", "小程序，按省分组，每天订单交易额"),

    TRADE_AMOUNT_CITY_PER_DAY_4_APP("b2bGroupId,marketCity", "小程序，按市分组，每天订单交易额"),

    TRADE_AMOUNT_BY_PAY_TYPE_PER_DAY_4_APP("b2bGroupId,payTypeId", "小程序，按支付方式分组，每天订单交易额"),

    TRADE_AMOUNT_BY_MARKET_ID_PER_DAY_4_APP("b2bGroupId,marketId", "小程序，按市场分组，天订单交易额"),

    TRADE_AMOUNT_BY_SELLER_PER_DAY_4_APP("b2bGroupId,sellerShopId", "小程序，按商家分组，每天订单交易额"),

    TRADE_AMOUNT_PER_HOUR_4_QM("", "千米，每小时订单交易额"),

    TRADE_AMOUNT_PER_DAY_4_QM("", "千米，每天订单交易额"),

    TRADE_AMOUNT_PROVINCE_PER_DAY_4_QM("marketProvince", "千米，按省分组，每天订单交易额"),

    TRADE_AMOUNT_CITY_PER_DAY_4_QM("marketCity", "千米，按市分组，每天订单交易额"),

    TRADE_AMOUNT_BY_PAY_TYPE_PER_DAY_4_QM("payTypeId", "千米，按支付方式分组，每天订单交易额"),

    TRADE_AMOUNT_BY_APP_ID_PER_DAY_4_QM("b2bGroupId", "千米，按小程序分组，每天订单交易额"),

    TRADE_AMOUNT_BY_MARKET_ID_PER_HOUR("marketId", "千米，按市场分组，每小时订单交易额"),

    TRADE_AMOUNT_BY_MARKET_ID_PER_DAY_4_QM("marketId", "千米，按市场分组，每天订单交易额"),

    TRADE_AMOUNT_BY_SELLER_PER_DAY_4_QM("sellerShopId", "千米，按商家分组，每天订单交易额"),

    TRADE_AMOUNT_BY_PAY_TYPE_BY_MARKET_ID_PER_DAY("marketId,payTypeId", "市场，按支付方式分组，每天订单交易额"),

    TRADE_AMOUNT_BY_SELLER_ID_PER_DAY("marketId,sellerShopId", "市场，按商家分组，每天订单交易额"),

    TRADE_AMOUNT_BUYER_PROVINCE_BY_MARKET_ID_PER_DAY("marketId,consigneeProvince", "市场，按收货人地址(省)分组，每天订单交易额"),

    TRADE_AMOUNT_BUYER_CITY_BY_MARKET_ID_PER_DAY("marketId,consigneeCity", "市场，按收货人地址(市)分组，每天订单交易额"),

    TRADE_AMOUNT_BY_BUYER_PER_DAY_4_MARKET("marketId,buyerUserId", "市场，按客户分组，每天订单交易额"),

    TRADE_AMOUNT_BY_SALE_MAN_PER_DAY_4_MARKET("marketId,marketSaleManId", "市场，按客户经理分组，每天订单交易额"),

    DISTINCT_BUYER_HAVE_TRADE_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时成交客户数(去重)"),

    DISTINCT_BUYER_HAVE_TRADE_PER_DAY_4_APP("b2bGroupId", "小程序，每天成交客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_PER_DAY_4_APP("b2bGroupId", "小程序，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_BY_PROVINCE_PER_DAY_4_APP("b2bGroupId,marketProvince", "小程序，按省分组，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_BY_CITY_PER_DAY_4_APP("b2bGroupId,marketCity", "小程序，按市分组，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_HAVE_TRADE_PER_HOUR_4_QM("", "千米，每小时成交客户数(去重)"),

    DISTINCT_BUYER_HAVE_TRADE_PER_DAY_4_QM("", "千米，每天成交客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_PER_DAY_4_QM("", "千米，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_BY_PROVINCE_PER_DAY_4_QM("marketProvince", "千米，按省分组，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_BY_CITY_PER_DAY_4_QM("marketCity", "千米，按市分组，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_HAVE_TRADE_PER_HOUR("marketId", "市场，每小时成交客户数(去重)"),

    DISTINCT_BUYER_HAVE_TRADE_PER_DAY("marketId", "市场，每天成交总客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_PER_DAY_4_MARKET("marketId", "市场，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_BY_PROVINCE_PER_DAY_4_MARKET("marketId,consigneeProvince", "市场，按省分组，每天成交新(首单)客户数(去重)"),

    DISTINCT_BUYER_FIRST_TRADE_BY_CITY_PER_DAY_4_MARKET("marketId,consigneeCity", "市场，按市分组，每天成交新(首单)客户数(去重)"),

    TRADE_COUNT_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时成交订单数"),

    TRADE_COUNT_PER_DAY_4_APP("b2bGroupId", "小程序，每天成交订单数"),

    TRADE_COUNT_PROVINCE_PER_DAY_4_APP("b2bGroupId,marketProvince", "小程序，按省分组，每天成交订单数"),

    TRADE_COUNT_CITY_PER_DAY_4_APP("b2bGroupId,marketCity", "小程序，按市分组，每天成交订单数"),

    TRADE_COUNT_BY_MARKET_ID_PER_DAY_4_APP("b2bGroupId,marketId", "小程序，按市场分组，每天成交订单数"),

    TRADE_COUNT_BY_SELLER_PER_DAY_4_APP("b2bGroupId,sellerShopId", "小程序，按商家店铺分组，每天成交订单数"),

    TRADE_COUNT_PER_HOUR_4_QM("", "千米，每小时成交订单数"),

    TRADE_COUNT_PER_DAY_4_QM("", "千米，每天成交订单数"),

    TRADE_COUNT_PROVINCE_PER_DAY_4_QM("marketProvince", "千米，按省分组，每天成交订单数"),

    TRADE_COUNT_CITY_PER_DAY_4_QM("marketCity", "千米，按市分组，每天成交订单数"),

    TRADE_COUNT_BY_APP_ID_PER_DAY_4_QM("b2bGroupId", "千米，按小程序分组，每天成交订单数"),

    TRADE_COUNT_BY_MARKET_ID_PER_DAY_4_QM("marketId", "千米，按市场分组，每天成交订单数"),

    TRADE_COUNT_BY_SELLER_PER_DAY_4_QM("sellerShopId", "千米，按商家店铺分组，每天成交订单数"),

    TRADE_COUNT_BY_MARKET_ID_PER_HOUR("marketId", "市场，每小时成交订单数"),

    TRADE_COUNT_BY_SELLER_ID_PER_DAY("marketId,sellerShopId", "市场，按商家店铺分组，每天成交订单数"),

    TRADE_COUNT_BY_BUYER_PER_DAY_4_MARKET("marketId,buyerUserId", "市场，按客户分组，每天成交订单数"),

    TRADE_COUNT_BY_SALE_MAN_PER_DAY_4_MARKET("marketId,marketSaleManId", "市场，按客户经理分组，每天成交订单数"),

    TRADE_ITEM_AMOUNT_BY_CAT_ID_PER_DAY_4_APP("b2bGroupId,marketIdAndCatIdList", "小程序，按市场商品分类catId分组，每天订单交易额"),

    TRADE_ITEM_AMOUNT_BY_CAT_ID_PER_DAY_4_QM("marketIdAndCatIdList", "千米，按市场商品分类catId分组，每天订单交易额"),

    TRADE_ITEM_AMOUNT_BY_MARKET_ID_PER_DAY("marketId,skuId", "市场，按SKU分组，每天订单交易额"),

    TRADE_ITEM_CATS_AMOUNT_BY_MARKET_ID_PER_DAY("marketId,catIds", "市场，按分类分组，每天订单交易额"),

    TRADE_ITEM_COUNT_BY_SKU_PER_DAY_4_APP("b2bGroupId,skuId", "小程序，按SKU分组，每天成交销量"),

    TRADE_ITEM_COUNT_PER_DAY_4_APP("b2bGroupId", "小程序，每天成交总SKU销量"),

    TRADE_ITEM_COUNT_BY_CAT_ID_PER_DAY_4_APP("b2bGroupId,marketIdAndCatIdList", "小程序，每天市场分类下成交总SKU销量"),

    TRADE_ITEM_COUNT_BY_SKU_PER_DAY_4_QM("skuId", "千米，按SKU分组，每天成交销量"),

    TRADE_ITEM_COUNT_BY_CAT_ID_PER_DAY_4_QM("marketIdAndCatIdList", "千米，按市场商品分类catId分组，每天成交销量"),

    TRADE_ITEM_COUNT_BY_MARKET_ID_PER_HOUR("marketId,skuId", "市场，按SKU分组，每小时成交销量"),

    TRADE_ITEM_COUNT_BY_MARKET_ID_PER_DAY("marketId,skuId", "市场，按SKU分组，每天成交销量"),

    TRADE_ITEM_COUNT_PER_DAY_4_MARKET("marketId", "市场，每天成交总SKU销量"),

    DISTINCT_TRADE_SKU_DAY_4_APP("b2bGroupId", "小程序，每天成交sku品种数(去重)"),

    DISTINCT_TRADE_SKU_DAY_4_MARKET("marketId", "市场，每天成交sku品种数(去重)"),

    DISTINCT_MARKET_HAVE_TRADE_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时成交市场数(去重)"),

    DISTINCT_MARKET_HAVE_TRADE_PER_DAY_4_APP("b2bGroupId", "小程序，每天成交市场数(去重)"),

    DISTINCT_MARKET_HAVE_TRADE_PER_HOUR_4_QM("", "千米，每小时成交市场数(去重)"),

    DISTINCT_MARKET_HAVE_TRADE_PER_DAY_4_QM("", "千米，每天成交市场数(去重)"),

    DISTINCT_SELLER_HAVE_TRADE_PER_HOUR_4_APP("b2bGroupId", "小程序，每小时成交商家店铺数(去重)"),

    DISTINCT_SELLER_HAVE_TRADE_PER_DAY_4_APP("b2bGroupId", "小程序，每天成交商家店铺数(去重)"),

    DISTINCT_SELLER_HAVE_TRADE_PER_HOUR_4_QM("", "千米，每小时成交商家店铺数(去重)"),

    DISTINCT_SELLER_HAVE_TRADE_PER_DAY_4_QM("", "千米，每天成交商家店铺数(去重)"),

    DISTINCT_SELLER_HAVE_TRADE_PER_HOUR("marketId", "市场，每小时成交商家店铺数(去重)"),

    DISTINCT_SELLER_HAVE_TRADE_PER_DAY("marketId", "市场，每天成交商家店铺数(去重)"),

    DISTINCT_TRADE_SELLER_PROVINCE_PER_DAY_4_MARKET("marketId,consigneeProvince", "市场，按省(收货人)分组，每天成交商家店铺数(去重)"),

    DISTINCT_TRADE_SELLER_CITY_PER_DAY_4_MARKET("marketId,consigneeCity", "市场，按市(收货人)分组，每天成交商家店铺数(去重)");

    /**
     * 使用时参考key值。
     * 支持多级嵌套结构，如: 
     * appId --> marketId ==> 销售额
     * 此处配置为: [appId,marketId]
     * <p>
     * 底层存储将直接平铺成map ==>
     * mapName: 任务ID|周期|appId
     * key: marketId
     * value: 销售额
     * <p>
     * 性能等因素综合考虑，目前限制：
     * 1、配置上目前尽量仅配置成最多2级结构（appId为第1级，marketId为第2级）；
     * 2、多级结构时，必须keys必须全传或仅允许少传最后一个key。
     * 如上述例子：可以传 appId（返回这个appId下所有市场的销售额）；也可以传 appId,market（返回找个appId指定的单个市场marketId的销售额）。
     * 如果后续有需要支持不传appId查询的，需要底层数据存储(目前为redis数据，lua脚本操作)调整为支持多级嵌套map结构【目前没有这方面的需求，暂不做支持】。
     */
    private final String keys;

    /**
     * 指标任务描述
     */
    private final String desc;

    StatTaskIds(String keys, String desc) {
        this.keys = keys;
        this.desc = desc;
    }

    public String getKeys() {
        return this.keys;
    }

    public String getDesc() {
        return this.desc;
    }

    public static StatTaskIds of(final String code) {
        if (null == code || code.trim().length() == 0) {
            return null;
        }
        for (StatTaskIds value : values()) {
            if (value.name().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
