package com.lrcortizo.document.manager.xlsx.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StreamManagerTest {

    @Test
    void given_splitIterator_should_return_stream_when_obtainStreamFromIterator() {
        // Given
        final List<String> givenStrings = List.of("G", "D", "F");

        // When
        final Stream<String> actualStream = StreamManager.obtainStreamFromIterator(givenStrings.spliterator());

        // Then
        assertThat(actualStream).isNotNull()
                .isEqualTo(givenStrings);
    }
}