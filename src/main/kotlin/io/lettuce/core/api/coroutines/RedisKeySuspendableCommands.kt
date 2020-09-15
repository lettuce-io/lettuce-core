/*
 * Copyright 2017-2020 the original author or authors.
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

@file:Suppress("unused")

package io.lettuce.core.api.coroutines

import io.lettuce.core.*
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Coroutine executed commands for Keys (Key manipulation/querying).
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Mikhael Sokolov
 * @since 6.0
 *
 * @generated by io.lettuce.apigenerator.CreateKotlinCoroutinesApi
 */
@ExperimentalLettuceCoroutinesApi
interface RedisKeySuspendableCommands<K, V> {

    /**
     * Delete one or more keys.
     *
     * @param keys the keys.
     * @return Long integer-reply The number of keys that were removed.
     *
     */
    suspend fun del(vararg keys: K): Long?

    /**
     * Unlink one or more keys (non blocking DEL).
     *
     * @param keys the keys.
     * @return Long integer-reply The number of keys that were removed.
     *
     */
    suspend fun unlink(vararg keys: K): Long?

    /**
     * Return a serialized version of the value stored at the specified key.
     *
     * @param key the key.
     * @return byte[] bulk-string-reply the serialized value.
     *
     */
    suspend fun dump(key: K): ByteArray?

    /**
     * Determine how many keys exist.
     *
     * @param keys the keys.
     * @return Long integer-reply specifically: Number of existing keys.
     *
     */
    suspend fun exists(vararg keys: K): Long?

    /**
     * Set a key's time to live in seconds.
     *
     * @param key the key.
     * @param seconds the seconds type: long.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if the timeout was set. {@code false} if {@code key} does not exist or the timeout could not be set.
     *
     */
    suspend fun expire(key: K, seconds: Long): Boolean?

    /**
     * Set the expiration for a key as a UNIX timestamp.
     *
     * @param key the key.
     * @param timestamp the timestamp type: posix time.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if the timeout was set. {@code false} if {@code key} does not exist or the timeout could not be set
     *         (see: {@code EXPIRE}).
     *
     */
    suspend fun expireat(key: K, timestamp: Date): Boolean?

    /**
     * Set the expiration for a key as a UNIX timestamp.
     *
     * @param key the key.
     * @param timestamp the timestamp type: posix time.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if the timeout was set. {@code false} if {@code key} does not exist or the timeout could not be set
     *         (see: {@code EXPIRE}).
     *
     */
    suspend fun expireat(key: K, timestamp: Long): Boolean?

    /**
     * Find all keys matching the given pattern.
     *
     * @param pattern the pattern type: patternkey (pattern).
     * @return List<K> array-reply list of keys matching {@code pattern}.
     *
     */
    suspend fun keys(pattern: K): Flow<K>?

    /**
     * Atomically transfer a key from a Redis instance to another one.
     *
     * @param host the host.
     * @param port the port.
     * @param key the key.
     * @param db the database.
     * @param timeout the timeout in milliseconds.
     * @return String simple-string-reply The command returns OK on success.
     *
     */
    suspend fun migrate(host: String, port: Int, key: K, db: Int, timeout: Long): String?

    /**
     * Atomically transfer one or more keys from a Redis instance to another one.
     *
     * @param host the host.
     * @param port the port.
     * @param db the database.
     * @param timeout the timeout in milliseconds.
     * @param migrateArgs migrate args that allow to configure further options.
     * @return String simple-string-reply The command returns OK on success.
     *
     */
    suspend fun migrate(host: String, port: Int, db: Int, timeout: Long, migrateArgs: MigrateArgs<K>): String?

    /**
     * Move a key to another database.
     *
     * @param key the key.
     * @param db the db type: long.
     * @return Boolean integer-reply specifically:.
     *
     */
    suspend fun move(key: K, db: Int): Boolean?

    /**
     * returns the kind of internal representation used in order to store the value associated with a key.
     *
     * @param key the key.
     * @return String.
     *
     */
    suspend fun objectEncoding(key: K): String?

    /**
     * returns the number of seconds since the object stored at the specified key is idle (not requested by read or write
     * operations).
     *
     * @param key the key.
     * @return number of seconds since the object stored at the specified key is idle.
     *
     */
    suspend fun objectIdletime(key: K): Long?

    /**
     * returns the number of references of the value associated with the specified key.
     *
     * @param key the key.
     * @return Long.
     *
     */
    suspend fun objectRefcount(key: K): Long?

    /**
     * Remove the expiration from a key.
     *
     * @param key the key.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if the timeout was removed. {@code false} if {@code key} does not exist or does not have an
     *         associated timeout.
     *
     */
    suspend fun persist(key: K): Boolean?

    /**
     * Set a key's time to live in milliseconds.
     *
     * @param key the key.
     * @param milliseconds the milliseconds type: long.
     * @return integer-reply, specifically:
     *
     *         {@code true} if the timeout was set. {@code false} if {@code key} does not exist or the timeout could not be set.
     *
     */
    suspend fun pexpire(key: K, milliseconds: Long): Boolean?

    /**
     * Set the expiration for a key as a UNIX timestamp specified in milliseconds.
     *
     * @param key the key.
     * @param timestamp the milliseconds-timestamp type: posix time.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if the timeout was set. {@code false} if {@code key} does not exist or the timeout could not be set
     *         (see: {@code EXPIRE}).
     *
     */
    suspend fun pexpireat(key: K, timestamp: Date): Boolean?

    /**
     * Set the expiration for a key as a UNIX timestamp specified in milliseconds.
     *
     * @param key the key.
     * @param timestamp the milliseconds-timestamp type: posix time.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if the timeout was set. {@code false} if {@code key} does not exist or the timeout could not be set
     *         (see: {@code EXPIRE}).
     *
     */
    suspend fun pexpireat(key: K, timestamp: Long): Boolean?

    /**
     * Get the time to live for a key in milliseconds.
     *
     * @param key the key.
     * @return Long integer-reply TTL in milliseconds, or a negative value in order to signal an error (see the description
     *         above).
     *
     */
    suspend fun pttl(key: K): Long?

    /**
     * Return a random key from the keyspace.
     *
     * @return K bulk-string-reply the random key, or {@code null} when the database is empty.
     *
     */
    suspend fun randomkey(): K?

    /**
     * Rename a key.
     *
     * @param key the key.
     * @param newKey the newkey type: key.
     * @return String simple-string-reply.
     *
     */
    suspend fun rename(key: K, newKey: K): String?

    /**
     * Rename a key, only if the new key does not exist.
     *
     * @param key the key.
     * @param newKey the newkey type: key.
     * @return Boolean integer-reply specifically:
     *
     *         {@code true} if {@code key} was renamed to {@code newkey}. {@code false} if {@code newkey} already exists.
     *
     */
    suspend fun renamenx(key: K, newKey: K): Boolean?

    /**
     * Create a key using the provided serialized value, previously obtained using DUMP.
     *
     * @param key the key.
     * @param ttl the ttl type: long.
     * @param value the serialized-value type: string.
     * @return String simple-string-reply The command returns OK on success.
     *
     */
    suspend fun restore(key: K, ttl: Long, value: ByteArray): String?

    /**
     * Create a key using the provided serialized value, previously obtained using DUMP.
     *
     * @param key the key.
     * @param value the serialized-value type: string.
     * @param args the {@link RestoreArgs}, must not be {@code null}.
     * @return String simple-string-reply The command returns OK on success.
     * @since 5.1
     *
     */
    suspend fun restore(key: K, value: ByteArray, args: RestoreArgs): String?

    /**
     * Sort the elements in a list, set or sorted set.
     *
     * @param key the key.
     * @return List<V> array-reply list of sorted elements.
     *
     */
    suspend fun sort(key: K): Flow<V>

    /**
     * Sort the elements in a list, set or sorted set.
     *
     * @param key the key.
     * @param sortArgs sort arguments.
     * @return List<V> array-reply list of sorted elements.
     *
     */
    suspend fun sort(key: K, sortArgs: SortArgs): Flow<V>

    /**
     * Sort the elements in a list, set or sorted set.
     *
     * @param key the key.
     * @param sortArgs sort arguments.
     * @param destination the destination key to store sort results.
     * @return Long number of values.
     *
     */
    suspend fun sortStore(key: K, sortArgs: SortArgs, destination: K): Long?

    /**
     * Touch one or more keys. Touch sets the last accessed time for a key. Non-exsitent keys wont get created.
     *
     * @param keys the keys.
     * @return Long integer-reply the number of found keys.
     *
     */
    suspend fun touch(vararg keys: K): Long?

    /**
     * Get the time to live for a key.
     *
     * @param key the key.
     * @return Long integer-reply TTL in seconds, or a negative value in order to signal an error (see the description above).
     *
     */
    suspend fun ttl(key: K): Long?

    /**
     * Determine the type stored at key.
     *
     * @param key the key.
     * @return String simple-string-reply type of {@code key}, or {@code none} when {@code key} does not exist.
     *
     */
    suspend fun type(key: K): String?

    /**
     * Incrementally iterate the keys space.
     *
     * @return KeyScanCursor<K> scan cursor.
     *
     */
    suspend fun scan(): KeyScanCursor<K>?

    /**
     * Incrementally iterate the keys space.
     *
     * @param scanArgs scan arguments.
     * @return KeyScanCursor<K> scan cursor.
     *
     */
    suspend fun scan(scanArgs: ScanArgs): KeyScanCursor<K>?

    /**
     * Incrementally iterate the keys space.
     *
     * @param scanCursor cursor to resume from a previous scan, must not be {@code null}.
     * @param scanArgs scan arguments.
     * @return KeyScanCursor<K> scan cursor.
     *
     */
    suspend fun scan(scanCursor: ScanCursor, scanArgs: ScanArgs): KeyScanCursor<K>?

    /**
     * Incrementally iterate the keys space.
     *
     * @param scanCursor cursor to resume from a previous scan, must not be {@code null}.
     * @return KeyScanCursor<K> scan cursor.
     *
     */
    suspend fun scan(scanCursor: ScanCursor): KeyScanCursor<K>?

}

