package io.lettuce.core.dynamic;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.dynamic.batch.BatchException;
import io.lettuce.core.dynamic.batch.CommandBatching;
import io.lettuce.core.dynamic.parameter.ExecutionSpecificParameters;
import io.lettuce.core.internal.Futures;
import io.lettuce.core.protocol.AsyncCommand;
import io.lettuce.core.protocol.RedisCommand;

/**
 * Executable command that uses a {@link Batcher} for command execution.
 *
 * @author Mark Paluch
 * @since 5.0
 */
class BatchExecutableCommand implements ExecutableCommand {

    private final CommandMethod commandMethod;

    private final CommandFactory commandFactory;

    private final Batcher batcher;

    private final StatefulConnection<Object, Object> connection;

    private final ExecutionSpecificParameters parameters;

    private final boolean async;

    BatchExecutableCommand(CommandMethod commandMethod, CommandFactory commandFactory, Batcher batcher,
            StatefulConnection<Object, Object> connection) {

        this.commandMethod = commandMethod;
        this.commandFactory = commandFactory;
        this.batcher = batcher;
        this.parameters = (ExecutionSpecificParameters) commandMethod.getParameters();
        this.async = commandMethod.isFutureExecution();
        this.connection = connection;
    }

    @Override
    public Object execute(Object[] parameters) throws ExecutionException, InterruptedException {

        RedisCommand<Object, Object, Object> command = commandFactory.createCommand(parameters);

        CommandBatching batching = null;
        if (this.parameters.hasCommandBatchingIndex()) {
            batching = (CommandBatching) parameters[this.parameters.getCommandBatchingIndex()];
        }

        AsyncCommand<Object, Object, Object> asyncCommand = new AsyncCommand<>(command);

        if (async) {
            batcher.batch(asyncCommand, batching);
            return asyncCommand;
        }

        BatchTasks batchTasks = batcher.batch(asyncCommand, batching);

        return synchronize(batchTasks, connection);
    }

    protected static Object synchronize(BatchTasks batchTasks, StatefulConnection<Object, Object> connection) {

        if (batchTasks == BatchTasks.EMPTY) {
            return null;
        }

        Duration timeout = connection.getTimeout();

        BatchException exception = null;
        List<RedisCommand<?, ?, ?>> failures = null;
        for (RedisCommand<?, ?, ?> batchTask : batchTasks) {

            try {
                Futures.await(timeout, (RedisFuture) batchTask);
            } catch (Exception e) {
                if (exception == null) {
                    failures = new ArrayList<>();
                    exception = new BatchException(failures);
                }

                failures.add(batchTask);
                exception.addSuppressed(e);
            }
        }

        if (exception != null) {
            throw exception;
        }

        return null;
    }

    @Override
    public CommandMethod getCommandMethod() {
        return commandMethod;
    }

}
