package com.boiz.streaming.account.dto;

public record Signup(String email, String password) {
    public static Signup of(final String email, final String password) {
        return new Signup(email, password);
    }
}
