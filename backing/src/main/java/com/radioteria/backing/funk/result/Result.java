package com.radioteria.backing.funk.result;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Result<V, E> {

    abstract boolean isSuccess();

    public boolean isError() {
        return !isSuccess();
    }

    abstract E getError();

    abstract V get();

    abstract V orElse(V alternative);

    abstract V orElseGet(Supplier<V> supplier);

    abstract<U> Result<U, E> map(Function<? super V, ? extends U> mapper);

    abstract<U> Result<U, E> flatMap(Function<? super V, ? extends Result<U, E>> mapper);

    abstract Result<V, E> filter(Predicate<? super V> predicate, Supplier<E> errorSupplier);

    abstract<U> Result<V, U> mapError(Function<? super E, ? extends U> mapper);

}
