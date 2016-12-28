package com.radioteria.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

abstract public class FunctionalUtil implements Predicate {

    public static <T, S> Predicate<T> statefulPredicate(
            S initialState,
            BiFunction<S, T, Boolean> statefulPredicate,
            BiFunction<S, T, S> stateUpdate
    ) {

        AtomicReference<S> state = new AtomicReference<>(initialState);

        return item -> statefulPredicate.apply(
                state.getAndUpdate(s -> stateUpdate.apply(s, item)), item);

    }

    public static <S, T> BiFunction<S, T, S> operator(BinaryOperator<S> op, Function<T, S> changeFunction) {
        return (s, v) -> op.apply(s, changeFunction.apply(v));
    }

}
