package com.radioteria.backing.funk.result;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Success<R, E> extends Result<R, E> {

    private R result;

    public Success(R result) {
        this.result = result;
    }

    @Override
    R get() {
        return result;
    }

    @Override
    boolean isSuccess() {
        return true;
    }

    @Override
    E getError() throws IllegalStateException {
        throw new IllegalStateException("Result is successful.");
    }

    @Override
    R orElse(R alternative) {
        return get();
    }

    @Override
    R orElseGet(Supplier<R> supplier) {
        return get();
    }

    @Override
    <U> Result<U, E> map(Function<? super R, ? extends U> mapper) {
        return new Success<>(mapper.apply(get()));
    }

    @Override
    <U> Result<U, E> flatMap(Function<? super R, ? extends Result<U, E>> mapper) {
        return mapper.apply(get());
    }

    @Override
    Result<R, E> filter(Predicate<? super R> predicate, Supplier<E> errorSupplier) {
        return predicate.test(get()) ? this : new Error<>(errorSupplier.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    <U> Result<R, U> mapError(Function<? super E, ? extends U> mapper) {
        return (Result<R, U>) this;
    }

}
