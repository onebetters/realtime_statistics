package com.zzc.micro.stat.core.config.metadata;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@FieldNameConstants
public class MeasureMetadata implements Serializable {
    private static final long serialVersionUID = 1478596577695601142L;

    /**
     * 任务ID
     */
    private String id;

    /**
     * 唯一编号，对外使用
     */
    private String code;

    /**
     * 周期（分钟）
     */
    private int period;

    /**
     * 支持的事件类型
     */
    private String event;

    /**
     * 配置示例:
     * 空: 底层转换为map，map字段为任务ID|周期，key为默认的空值，value值为对应指标;
     * key1: 底层转换为map，map字段为任务ID|周期，key为key1，value值为对应指标值;
     * key1,key2: 底层转换为map，map字段为任务ID|周期|key1，key为key2，value值为对应指标值;
     * key1...keyN: 底层转换为map，map字段为任务ID|周期|key1|..|key(N-1)，key为keyN，value值为对应指标值;
     * <p>
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
    private String keys;

    /**
     * hash value字段值
     */
    private String value;

    /**
     * value字段特殊过滤器
     */
    private String condition;

    /**
     * 描述信息
     */
    private String desc;

    /**
     * 归集器
     */
    private String aggregator;

    @Nonnull
    public String[] collectKeys() {
        if (StringUtils.isBlank(keys)) {
            return new String[0];
        }
        return org.springframework.util.StringUtils.tokenizeToStringArray(keys, ",;");
    }

    public int workFieldsHashCode() {
        return HashCodeBuilder.reflectionHashCode(this, Fields.id, Fields.code, Fields.desc);
    }
}
