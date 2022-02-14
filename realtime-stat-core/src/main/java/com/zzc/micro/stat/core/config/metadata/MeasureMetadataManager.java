package com.zzc.micro.stat.core.config.metadata;

import com.alibaba.fastjson.JSON;
import com.zzc.micro.stat.core.config.StatProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class MeasureMetadataManager {

    private final MeasureMetadataResourceLoader resourceLoader;
    private final AtomicBoolean init = new AtomicBoolean();
    private final List<MeasureMetadata> measures = new ArrayList<>();
    private final Map<String, MeasureMetadata> mapper = new ConcurrentHashMap<>(64);

    @Autowired
    public MeasureMetadataManager(StatProperties statProperties) {
        this(statProperties.getMeasure().getMeasureFiles());
    }

    public MeasureMetadataManager(final String measureFileRegex) {
        this.resourceLoader = new MeasureMetadataResourceLoader(measureFileRegex);
    }

    public List<MeasureMetadata> getMeasures() {
        if (!init.get()) {
            synchronized (this) {
                if (!init.get()) {
                    log.debug("开始初始化指标任务");
                    final Resource[] resources = resourceLoader.getResources();
                    log.debug("搜索到指标配置文件: {}", Arrays.stream(resources).collect(Collectors.toList()));
                    final List<MeasureMetadata> loadMeasures = Arrays.stream(resources).flatMap(this::load).collect(Collectors.toList());
                    log.debug("加载生效的指标任务，共计{}个", loadMeasures.size());

                    final List<String> ids = loadMeasures.stream().map(MeasureMetadata::getId).collect(Collectors.toList());
                    final Set<String> duplicateIds = ids.stream().filter(i -> Collections.frequency(ids, i) > 1).collect(Collectors.toSet());
                    Validate.isTrue(duplicateIds.isEmpty(), "指标配置存在重复的id项，重复id为: " + String.join(",", duplicateIds));


                    final List<String> codes = loadMeasures.stream().map(MeasureMetadata::getCode).collect(Collectors.toList());
                    final Set<String> duplicateCodes = codes.stream().filter(i -> Collections.frequency(codes, i) > 1).collect(Collectors.toSet());
                    Validate.isTrue(duplicateCodes.isEmpty(), "指标配置存在重复的code项，重复code为: " + String.join(",", duplicateCodes));


                    final List<Integer> checkDuplicateConf = loadMeasures.stream().map(MeasureMetadata::workFieldsHashCode).collect(Collectors.toList());
                    final Set<Integer> mayDuplicateMeasureWorkHash = checkDuplicateConf.stream()
                            .filter(i -> Collections.frequency(checkDuplicateConf, i) > 1)
                            .collect(Collectors.toSet());
                    if (!mayDuplicateMeasureWorkHash.isEmpty()) {
                        log.warn("可能存在相似的任务配置，相似任务任务为: \n\t{}",
                                mayDuplicateMeasureWorkHash.stream()
                                        .map(hash -> loadMeasures.stream()
                                                .filter(m -> hash == m.workFieldsHashCode())
                                                .map(m -> m.getId() + "[" + m.getCode() + "]")
                                                .collect(Collectors.joining(", ")))
                                        .collect(Collectors.joining(";\n\t")));
                    }


                    measures.clear();
                    measures.addAll(loadMeasures);

                    loadMeasures.forEach(measure -> {
                        mapper.put(measure.getId(), measure);
                        mapper.put(measure.getCode(), measure);
                    });
                    log.debug("指标任务初始化完成");
                }
            }
        }
        return measures;
    }

    public Optional<MeasureMetadata> findByIdOrCode(final String idOrCode) {
        return Optional.ofNullable(mapper.get(idOrCode));
    }

    private Stream<MeasureMetadata> load(final Resource resource) {
        if (!resource.exists()) {
            log.debug("指标配置资源文件不存在，忽略该配置文件, resource={}", resource);
            return Stream.empty();
        }
        if (!resource.isReadable()) {
            log.debug("指标配置资源文件不可读，忽略该配置文件, resource={}", resource);
            return Stream.empty();
        }

        try {
            final InputStream inputStream = resource.getInputStream();
            final String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return JSON.parseArray(json, MeasureMetadata.class).stream();
        } catch (IOException e) {
            log.warn("指标文件配置内容读取出现IO异常，resource={}", resource.getFilename(), e);
            return Stream.empty();
        }
    }
}
