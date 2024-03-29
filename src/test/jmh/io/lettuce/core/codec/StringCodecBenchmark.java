package io.lettuce.core.codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Benchmark for {@link StringCodec}.
 *
 * @author Mark Paluch
 */
public class StringCodecBenchmark {

    @Benchmark
    public void encodeUtf8Unpooled(Input input) {
        input.blackhole.consume(input.utf8Codec.encodeKey(input.teststring));
    }

    @Benchmark
    public void encodeUtf8ToBuf(Input input) {
        input.byteBuf.clear();
        input.utf8Codec.encode(input.teststring, input.byteBuf);
    }

    @Benchmark
    public void encodeUtf8PlainStringToBuf(Input input) {
        input.byteBuf.clear();
        input.utf8Codec.encode(input.teststringPlain, input.byteBuf);
    }

    @Benchmark
    public void encodeAsciiToBuf(Input input) {
        input.byteBuf.clear();
        input.asciiCodec.encode(input.teststringPlain, input.byteBuf);
    }

    @Benchmark
    public void encodeIsoToBuf(Input input) {
        input.byteBuf.clear();
        input.isoCodec.encode(input.teststringPlain, input.byteBuf);
    }

    @Benchmark
    public void decodeUtf8Unpooled(Input input) {
        input.input.rewind();
        input.blackhole.consume(input.utf8Codec.decodeKey(input.input));
    }

    @State(Scope.Thread)
    public static class Input {

        Blackhole blackhole;
        StringCodec asciiCodec = new StringCodec(StandardCharsets.US_ASCII);
        StringCodec utf8Codec = new StringCodec(StandardCharsets.UTF_8);
        StringCodec isoCodec = new StringCodec(StandardCharsets.ISO_8859_1);

        String teststring = "hello üäü~∑†®†ª€∂‚¶¢ Wørld";
        String teststringPlain = "hello uufadsfasdfadssdfadfs";
        ByteBuffer input = ByteBuffer.wrap(teststring.getBytes(StandardCharsets.UTF_8));

        ByteBuf byteBuf = Unpooled.buffer(512);

        @Setup
        public void setup(Blackhole bh) {
            blackhole = bh;
            input.flip();
        }
    }
}
