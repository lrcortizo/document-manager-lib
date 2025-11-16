package com.lrcortizo.document.manager.xlsx.utils;

import lombok.experimental.UtilityClass;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@UtilityClass
public class StreamManager {

    public <T> Stream<T> obtainStreamFromIterator(final Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }
}
