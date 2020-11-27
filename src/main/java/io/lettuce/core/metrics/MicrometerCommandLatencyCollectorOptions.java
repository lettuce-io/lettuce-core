/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.metrics;

import io.lettuce.core.internal.LettuceAssert;
import io.micrometer.core.instrument.Tags;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * The Micrometer implementation of {@link CommandLatencyCollectorOptions}.
 *
 * @author Steven Sheehy
 */
public class MicrometerCommandLatencyCollectorOptions implements CommandLatencyCollectorOptions {

    public static final boolean DEFAULT_ENABLED = true;

    public static final boolean DEFAULT_HISTOGRAM = false;

    public static final boolean DEFAULT_LOCAL_DISTINCTION = false;

    public static final Duration DEFAULT_MIN_LATENCY = Duration.ofMillis(1L);

    public static final Duration DEFAULT_MAX_LATENCY = Duration.ofMinutes(5L);

    public static final double[] DEFAULT_TARGET_PERCENTILES = new double[] { 0.50, 0.90, 0.95, 0.99, 0.999 };

    private static final MicrometerCommandLatencyCollectorOptions DISABLED = builder().disable().build();

    private final Builder builder;

    private final boolean enabled;

    private final boolean histogram;

    private final boolean localDistinction;

    private final Duration maxLatency;

    private final Duration minLatency;

    private final Tags tags;

    private final double[] targetPercentiles;

    protected MicrometerCommandLatencyCollectorOptions(Builder builder) {
        this.builder = builder;
        this.enabled = builder.enabled;
        this.histogram = builder.histogram;
        this.localDistinction = builder.localDistinction;
        this.maxLatency = builder.maxLatency;
        this.minLatency = builder.minLatency;
        this.tags = builder.tags;
        this.targetPercentiles = builder.targetPercentiles;
    }

    /**
     * Create a new {@link MicrometerCommandLatencyCollectorOptions} instance using default settings.
     *
     * @return a new instance of {@link MicrometerCommandLatencyCollectorOptions} instance using default settings
     */
    public static MicrometerCommandLatencyCollectorOptions create() {
        return builder().build();
    }

    /**
     * Create a {@link MicrometerCommandLatencyCollectorOptions} instance with disabled event emission.
     *
     * @return a new instance of {@link MicrometerCommandLatencyCollectorOptions} with disabled event emission
     */
    public static MicrometerCommandLatencyCollectorOptions disabled() {
        return DISABLED;
    }

    /**
     * Returns a new {@link MicrometerCommandLatencyCollectorOptions.Builder} to construct
     * {@link MicrometerCommandLatencyCollectorOptions}.
     *
     * @return a new {@link MicrometerCommandLatencyCollectorOptions.Builder} to construct
     *         {@link MicrometerCommandLatencyCollectorOptions}.
     */
    public static MicrometerCommandLatencyCollectorOptions.Builder builder() {
        return new MicrometerCommandLatencyCollectorOptions.Builder();
    }

    /**
     * Returns a builder to create new {@link MicrometerCommandLatencyCollectorOptions} whose settings are replicated from the
     * current {@link MicrometerCommandLatencyCollectorOptions}.
     *
     * @return a a {@link CommandLatencyCollectorOptions.Builder} to create new {@link MicrometerCommandLatencyCollectorOptions}
     *         whose settings are replicated from the current {@link MicrometerCommandLatencyCollectorOptions}
     *
     * @since 5.1
     */
    @Override
    public MicrometerCommandLatencyCollectorOptions.Builder mutate() {
        return this.builder;
    }

    /**
     * Builder for {@link MicrometerCommandLatencyCollectorOptions}.
     */
    public static class Builder implements CommandLatencyCollectorOptions.Builder {

        private boolean enabled = DEFAULT_ENABLED;

        private boolean histogram = DEFAULT_HISTOGRAM;

        private boolean localDistinction = DEFAULT_LOCAL_DISTINCTION;

        private Duration maxLatency = DEFAULT_MAX_LATENCY;

        private Duration minLatency = DEFAULT_MIN_LATENCY;

        private Tags tags = Tags.empty();

        private double[] targetPercentiles = DEFAULT_TARGET_PERCENTILES;

        private Builder() {
        }

        /**
         * Disable the latency collector.
         *
         * @return this {@link Builder}.
         */
        @Override
        public Builder disable() {
            this.enabled = false;
            return this;
        }

        /**
         * Enable the latency collector.
         *
         * @return this {@link Builder}.
         * @since 5.1
         */
        @Override
        public Builder enable() {
            this.enabled = true;
            return this;
        }

        /**
         * Enable histogram buckets used to generate aggregable percentile approximations in monitoring
         * systems that have query facilities to do so.
         *
         * @return this {@link Builder}.
         */
        public Builder histogram() {
            this.histogram = true;
            return this;
        }

        /**
         * Enables per connection metrics tracking insead of per host/port. If {@code true}, multiple connections to the same
         * host/connection point will be recorded separately which allows to inspect every connection individually. If
         * {@code false}, multiple connections to the same host/connection point will be recorded together. This allows a
         * consolidated view on one particular service. Defaults to {@code false}. See
         * {@link MicrometerCommandLatencyCollectorOptions#DEFAULT_LOCAL_DISTINCTION}.
         *
         * @param localDistinction {@code true} if latencies are recorded distinct on local level (per connection)
         * @return this {@link Builder}.
         */
        @Override
        public Builder localDistinction(boolean localDistinction) {
            this.localDistinction = localDistinction;
            return this;
        }

        /**
         * Sets the maximum value that this timer is expected to observe. Sets an upper bound
         * on histogram buckets that are shipped to monitoring systems that support aggregable percentile approximations.
         * Only applicable when histogram is enabled. Defaults to {@code 5m}.
         * See {@link MicrometerCommandLatencyCollectorOptions#DEFAULT_MAX_LATENCY}.
         *
         * @param maxLatency The maximum value that this timer is expected to observe.
         * @return this {@link Builder}.
         */
        public Builder maxLatency(Duration maxLatency) {
            this.maxLatency = maxLatency;
            return this;
        }

        /**
         * Sets the minimum value that this timer is expected to observe. Sets a lower bound
         * on histogram buckets that are shipped to monitoring systems that support aggregable percentile approximations.
         * Only applicable when histogram is enabled. Defaults to {@code 1ms}.
         * See {@link MicrometerCommandLatencyCollectorOptions#DEFAULT_MIN_LATENCY}.
         *
         * @param minLatency The minimum value that this timer is expected to observe.
         * @return this {@link Builder}.
         */
        public Builder minLatency(Duration minLatency) {
            this.minLatency = minLatency;
            return this;
        }

        /**
         * Not supported since the MeterRegistry implementation defines whether metrics are cumulative or reset
         *
         * @param resetLatenciesAfterEvent {@code true} if the recorded latencies should be reset once the metrics event was
         *        emitted
         * @return this {@link Builder}.
         */
        @Override
        public Builder resetLatenciesAfterEvent(boolean resetLatenciesAfterEvent) {
            throw new UnsupportedOperationException("resetLatenciesAfterEvent not supported for Micrometer");
        }

        /**
         * Extra tags to add to the generated metrics. Defaults to {@code Tags.empty()}.
         *
         * @param tags Tags to add to the metrics.
         * @return this {@link Builder}.
         */
        public Builder tags(Tags tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Sets the emitted percentiles. Defaults to 0.50, 0.90, 0.95, 0.99, 0.999}. Only applicable when histogram is enabled.
         * See {@link MicrometerCommandLatencyCollectorOptions#DEFAULT_TARGET_PERCENTILES}.
         *
         * @param targetPercentiles the percentiles which should be emitted, must not be {@code null}
         * @return this {@link Builder}.
         */
        @Override
        public Builder targetPercentiles(double[] targetPercentiles) {
            LettuceAssert.notNull(targetPercentiles, "TargetPercentiles must not be null");
            this.targetPercentiles = targetPercentiles;
            return this;
        }

        /**
         * Not supported since the MeterRegistry implementation defines the base unit and cannot be changed.
         *
         * @param targetUnit the target unit
         * @return this {@link Builder}.
         */
        @Override
        public Builder targetUnit(TimeUnit targetUnit) {
            throw new UnsupportedOperationException("targetUnit not supported for Micrometer");
        }

        /**
         * @return a new instance of {@link MicrometerCommandLatencyCollectorOptions}.
         */
        @Override
        public MicrometerCommandLatencyCollectorOptions build() {
            return new MicrometerCommandLatencyCollectorOptions(this);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHistogram() {
        return histogram;
    }

    @Override
    public boolean localDistinction() {
        return localDistinction;
    }

    public Duration maxLatency() {
        return maxLatency;
    }

    public Duration minLatency() {
        return minLatency;
    }

    @Override
    public boolean resetLatenciesAfterEvent() {
        throw new UnsupportedOperationException("resetLatenciesAfterEvent not supported for Micrometer");
    }

    public Tags tags() {
        return tags;
    }

    @Override
    public double[] targetPercentiles() {
        double[] result = new double[targetPercentiles.length];
        System.arraycopy(targetPercentiles, 0, result, 0, targetPercentiles.length);
        return result;
    }

    @Override
    public TimeUnit targetUnit() {
        throw new UnsupportedOperationException("targetUnit not supported for Micrometer");
    }
}
