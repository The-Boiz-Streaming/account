package com.boiz.streaming.account.util;

public class Args {

    public static <T> T nonNullOrDefault(final T value, final T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
