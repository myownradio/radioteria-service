package com.radioteria.util;

public class MathUtil {

    public static boolean between(long left, long right, long value) {
        return left < value && right > value;
    }

    public static boolean inRange(long left, long right, long value) {
        return left <= value && right > value;
    }

}
