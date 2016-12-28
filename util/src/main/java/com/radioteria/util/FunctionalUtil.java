package com.radioteria.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Predicate;

abstract public class FunctionalUtil implements Predicate {

    public static <T, S> Predicate<T> statefulFilter(
            S initialState,
            BiFunction<S, T, Boolean> statefulPredicate,
            BiFunction<S, T, S> stateUpdate
    ) {

        AtomicReference<S> state = new AtomicReference<>(initialState);

        return item -> statefulPredicate.apply(
                state.getAndUpdate(s -> stateUpdate.apply(s, item)), item);

    }

}
