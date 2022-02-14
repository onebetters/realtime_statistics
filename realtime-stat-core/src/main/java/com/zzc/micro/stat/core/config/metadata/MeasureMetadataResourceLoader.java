package com.zzc.micro.stat.core.config.metadata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Slf4j
public class MeasureMetadataResourceLoader {

    private final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
    private final String basePackages;

    public MeasureMetadataResourceLoader(String basePackages) {
        this.basePackages = basePackages;
    }

    @Nonnull
    public Resource[] getResources() {
        final String[] packages = StringUtils.tokenizeToStringArray(basePackages, ",;");
        return Arrays.stream(packages).flatMap(this::find).toArray(Resource[]::new);
    }

    private Stream<Resource> find(final String fileRegex) {
        try {
            return Arrays.stream(patternResolver.getResources(fileRegex));
        } catch (IOException e) {
            log.warn("指标文件加载出现IO异常, file={}", fileRegex, e);
            return Stream.empty();
        }
    }
}
