package com.radioteria.util;

import java.util.Arrays;
import java.util.Optional;

public class OptionalUtil {

    @SafeVarargs
    public static <X> Optional<X> first(Optional<X> ...optionals) {
        return Arrays.stream(optionals)
                .reduce(Optional.empty(), (acc, opt) -> acc.isPresent() ? acc : opt);
    }

    @SafeVarargs
    public static <X> Optional<X> last(Optional<X> ...optionals) {
        return Arrays.stream(optionals)
                .reduce(Optional.empty(), (acc, opt) -> opt.isPresent() ? opt : acc);
    }

}
