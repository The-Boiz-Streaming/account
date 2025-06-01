package com.boiz.streaming.account.dto;

public record Login(String email, String password) {
    public static Login of(final String email, final String password) {
        return new Login(email, password);
    }
}
