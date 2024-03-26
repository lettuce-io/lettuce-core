package io.lettuce.core.output;

import static org.assertj.core.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import io.lettuce.core.GeoCoordinates;
import io.lettuce.core.Value;
import io.lettuce.core.codec.StringCodec;

/**
 * @author Mark Paluch
 */
class GeoCoordinatesValueListOutputUnitTests {

    private GeoCoordinatesValueListOutput<?, ?> sut = new GeoCoordinatesValueListOutput<>(StringCodec.UTF8);

    @Test
    void defaultSubscriberIsSet() {
        assertThat(sut.getSubscriber()).isNotNull().isInstanceOf(ListSubscriber.class);
    }

    @Test
    void setIntegerShouldFail() {
        assertThatThrownBy(() -> sut.set(123L)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void commandOutputCorrectlyDecoded() {

        sut.multi(2);
        sut.set(ByteBuffer.wrap("1.234".getBytes()));
        sut.set(ByteBuffer.wrap("4.567".getBytes()));
        sut.multi(-1);

        assertThat(sut.get()).contains(Value.just(new GeoCoordinates(1.234, 4.567)));
    }
}
