package com.zzc.micro.stat;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * B2B统计接口
 */
public interface B2BStatQueryProvider {

    @Nonnull
    List<StatQueryResult> query(@Nonnull StatQueryRequest request);
}
