package io.lettuce.core.tracing;

import io.lettuce.core.protocol.RedisCommand;

/**
 * Tracing abstraction to create {@link Span}s to capture latency and behavior of Redis commands.
 *
 * @author Mark Paluch
 * @since 5.1
 * @see Span
 */
public abstract class Tracer {

    /**
     * Returns a new trace {@link Tracer.Span}.
     *
     * @return a new {@link Span}.
     */
    public abstract Span nextSpan();

    /**
     * Returns a new trace {@link Tracer.Span} associated with {@link TraceContext} or a new one if {@link TraceContext} is
     * {@code null}.
     *
     * @param traceContext the trace context.
     * @return a new {@link Span}.
     */
    public abstract Span nextSpan(TraceContext traceContext);

    /**
     * Used to model the latency of an operation along with tags such as name or the {@link Tracing.Endpoint}.
     */
    public abstract static class Span {

        /**
         * Starts the span with.
         *
         * @param command the underlying command.
         * @return {@literal this} {@link Span}.
         */
        public abstract Span start(RedisCommand<?, ?, ?> command);

        /**
         * Sets the name for this {@link Span}.
         *
         * @param name must not be {@code null}.
         * @return {@literal this} {@link Span}.
         */
        public abstract Span name(String name);

        /**
         * Associates an event that explains latency with the current system time.
         *
         * @param value A short tag indicating the event, like "finagle.retry"
         */
        public abstract Span annotate(String value);

        /**
         * Associates a tag with this {@link Span}.
         *
         * @param key must not be {@code null}.
         * @param value must not be {@code null}.
         * @return {@literal this} {@link Span}.
         */
        public abstract Span tag(String key, String value);

        /**
         * Associate an {@link Throwable error} with this {@link Span}.
         *
         * @param throwable must not be {@code null}.
         * @return {@literal this} {@link Span}.
         */
        public abstract Span error(Throwable throwable);

        /**
         * Associates an {@link Tracing.Endpoint} with this {@link Span}.
         *
         * @param endpoint must not be {@code null}.
         * @return {@literal this} {@link Span}.
         */
        public abstract Span remoteEndpoint(Tracing.Endpoint endpoint);

        /**
         * Reports the span complete.
         */
        public abstract void finish();

    }

}
