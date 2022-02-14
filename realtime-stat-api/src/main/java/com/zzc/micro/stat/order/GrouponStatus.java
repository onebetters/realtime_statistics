package com.zzc.micro.stat.order;

import lombok.Getter;

/**
 * 成团状态
 * @author Administrator
 */
@Getter
public enum GrouponStatus {
    NOT_GROUPED("NOT_GROUPED", "未成团"),
    GROUPING("GROUPING", "待成团"),
    GROUPED("GROUPED", "已成团"),
    CANCELED("CANCELED", "已取消"),
    FAILED("FAILED", "已失败");

    private String statusValue;

    private String description;

    GrouponStatus(String statusValue, String description) {
        this.statusValue = statusValue;
        this.description = description;
    }
}
