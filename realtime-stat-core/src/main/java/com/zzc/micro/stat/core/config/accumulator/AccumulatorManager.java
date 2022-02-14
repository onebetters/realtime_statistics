package com.zzc.micro.stat.core.config.accumulator;

import com.zzc.micro.stat.core.accumulator.Accumulator;
import com.zzc.micro.stat.core.config.metadata.MeasureMetadata;
import com.zzc.micro.stat.core.config.metadata.MeasureMetadataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class AccumulatorManager {

    private final Accumulator<?>[] accumulators;

    public AccumulatorManager(final MeasureMetadataManager measureMetadataManager) {
        final List<MeasureMetadata> measures = measureMetadataManager.getMeasures();
        this.accumulators = measures.stream().map(this::buildAggregator).toArray(Accumulator[]::new);
    }

    private Accumulator<?> buildAggregator(final MeasureMetadata measure) {
        return new AccumulatorBuilder(measure).build();
    }

    @Nonnull
    public Accumulator<?>[] getAggregators() {
        return accumulators;
    }
}
