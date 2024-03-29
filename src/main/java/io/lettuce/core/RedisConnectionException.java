package io.lettuce.core;

import java.net.SocketAddress;

/**
 * Exception for connection failures.
 *
 * @author Mark Paluch
 */
@SuppressWarnings("serial")
public class RedisConnectionException extends RedisException {

    /**
     * Create a {@code RedisConnectionException} with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RedisConnectionException(String msg) {
        super(msg);
    }

    /**
     * Create a {@code RedisConnectionException} with the specified detail message and nested exception.
     *
     * @param msg the detail message.
     * @param cause the nested exception.
     */
    public RedisConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create a new {@link RedisConnectionException} given {@link SocketAddress} and the {@link Throwable cause}.
     *
     * @param remoteAddress remote socket address.
     * @param cause the nested exception.
     * @return the {@link RedisConnectionException}.
     * @since 4.4
     */
    public static RedisConnectionException create(SocketAddress remoteAddress, Throwable cause) {
        return create(remoteAddress == null ? null : remoteAddress.toString(), cause);
    }

    /**
     * Create a new {@link RedisConnectionException} given {@code remoteAddress} and the {@link Throwable cause}.
     *
     * @param remoteAddress remote address.
     * @param cause the nested exception.
     * @return the {@link RedisConnectionException}.
     * @since 5.1
     */
    public static RedisConnectionException create(String remoteAddress, Throwable cause) {

        if (remoteAddress == null) {

            if (cause instanceof RedisConnectionException) {
                return new RedisConnectionException(cause.getMessage(), cause.getCause());
            }

            return new RedisConnectionException(null, cause);
        }

        return new RedisConnectionException(String.format("Unable to connect to %s", remoteAddress), cause);
    }

    /**
     * Create a new {@link RedisConnectionException} given {@link Throwable cause}.
     *
     * @param cause the exception.
     * @return the {@link RedisConnectionException}.
     * @since 5.1
     */
    public static RedisConnectionException create(Throwable cause) {

        if (cause instanceof RedisConnectionException) {
            return new RedisConnectionException(cause.getMessage(), cause.getCause());
        }

        return new RedisConnectionException("Unable to connect", cause);
    }

    /**
     * @param error the error message.
     * @return {@code true} if the {@code error} message indicates Redis protected mode.
     * @since 5.0.1
     */
    public static boolean isProtectedMode(String error) {
        return error != null && error.startsWith("DENIED");
    }

}
