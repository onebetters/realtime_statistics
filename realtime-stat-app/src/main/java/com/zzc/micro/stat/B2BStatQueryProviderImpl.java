package com.zzc.micro.stat;

import com.zzc.micro.stat.core.query.StatQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class B2BStatQueryProviderImpl implements B2BStatQueryProvider {

    private final StatQueryService statQueryService;

    @Override
    @Nonnull
    public List<StatQueryResult> query(@Nonnull StatQueryRequest request) {
        return statQueryService.query(request);
    }
}
