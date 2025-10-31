package com.bigdata.core;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface DataProcessor<T, R> {

    Stream<R> process(Stream<T> input);

    // Composition: Chains this processor with the next one
    default <V> DataProcessor<T, V> andThen(DataProcessor<R, V> next) {
        return input -> next.process(this.process(input));
    }

    // With error handling: Catches runtime exceptions in the stream
    default DataProcessor<T, R> withErrorHandling(Consumer<Exception> errorHandler) {
        return input -> this.process(input)
                .map(item -> {
                    try {
                        // Force the stream execution to happen within the try-catch block
                        return item;
                    } catch (Exception e) {
                        errorHandler.accept(e);
                        return null; // Return null to be filtered out
                    }
                })
                .filter(Objects::nonNull);
    }
}