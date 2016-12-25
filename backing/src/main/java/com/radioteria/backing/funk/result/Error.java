package com.radioteria.backing.funk.result;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Error<R, E> extends Result<R, E> {

    private E error;

    public Error(E exception) {
        this.error = exception;
    }

    @Override
    boolean isSuccess() {
        return false;
    }

    @Override
    R get() {
        throw new IllegalStateException("Result is failed.");
    }

    @Override
    R orElse(R alternative) {
        return alternative;
    }

    @Override
    R orElseGet(Supplier<R> supplier) {
        return supplier.get();
    }

    @Override
    E getError() {
        return error;
    }

    @Override
    @SuppressWarnings("unchecked")
    <U> Result<U, E> map(Function<? super R, ? extends U> mapper) {

        return (Result<U, E>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    <U> Result<U, E> flatMap(Function<? super R, ? extends Result<U, E>> mapper) {
        return (Result<U, E>) this;
    }

    @Override
    Result<R, E> filter(Predicate<? super R> predicate, Supplier<E> errorSupplier) {
        return this;
    }

    @Override
    <U> Result<R, U> mapError(Function<? super E, ? extends U> mapper) {
        return null;
    }

}


