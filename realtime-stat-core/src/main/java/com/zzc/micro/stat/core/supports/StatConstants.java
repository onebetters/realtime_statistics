package com.zzc.micro.stat.core.supports;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
public interface StatConstants {

    String KEY_DELIMITER = ":";
    String BUCKET_BIZ_DELIMITER = "|";
    String PREFIX_BASE = "A";
    String PREFIX_EVENT = String.join(KEY_DELIMITER, PREFIX_BASE, "E");
    String PREFIX_TASK = String.join(KEY_DELIMITER, PREFIX_BASE, "M");
    String NULL_MEMBER_KEY = "_U"; // UNKNOWN
    LocalDateTime START_DATE_TIME = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
}
