package com.zzc.micro.stat;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatQueryRequest implements Serializable {
    private static final long serialVersionUID = -3770088782898138893L;

    /**
     * 查询任务。必传
     * 即使{@link StatTaskIds 参考任务枚举范围}未定义，直接用不在枚举里面的字符串也是可以的
     *
     * @see StatTaskIds
     */
    @Singular
    private Collection<String> ids;

    /**
     * 开始时间，仅支持最近2天时间。非必传，默认结束时间的0点0分
     */
    private Date startTimeInclusive;

    /**
     * 结束时间，仅支持最近2天时间。非必传，默认当前时间
     */
    private Date endTimeInclusive;

    /**
     * 超过1级结构时使用。
     * <p>
     * 场景举例: appId --> 市场ID --> 销售额。
     * 底层存储可理解成平铺的map结构.
     * map名称: 任务ID|周期|appId, field: 市场ID, value: 销售额
     * <p>
     * 此处传appId
     */
    private String[] groups;

    /**
     * 过滤key值。非必传。
     * <p>
     * 结合{@link #groups 上述例子的场景}，此处传市场ID
     */
    private String key;

    /**
     * 批量查询时，每个任务topN（默认从大到小排序）最多查询的条数。
     * 未指定时，默认值100。
     * <p>
     * 一般用于排行榜数据。
     * 如，统计小程序销量最高的topN个商品
     */
    @Builder.Default
    private long limit = 50;

    @SuppressWarnings("unused")
    public static class StatQueryRequestBuilder {
        private String[] groups;

        public StatQueryRequestBuilder group(final String group) {
            if (StringUtils.isNotBlank(group)) {
                this.groups = Stream.of(group).toArray(String[]::new);
            }
            return this;
        }

        public StatQueryRequestBuilder groups(final String... groups) {
            final String[] groupsUse = Optional.ofNullable(groups).orElse(new String[0]);
            if (Arrays.stream(groupsUse).allMatch(StringUtils::isNotBlank)) {
                this.groups = groupsUse;
            }
            return this;
        }

        public StatQueryRequestBuilder groups(final Collection<String> groups) {
            this.groups = Optional.ofNullable(groups).orElse(Collections.emptyList()).toArray(new String[0]);
            return this;
        }
    }
}
