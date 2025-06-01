package com.boiz.streaming.account.dto;

public record TokenPair(String accessToken, String refreshToken) {
    public static TokenPair of(final String accessToken, final String refreshToken) {
        return new TokenPair(accessToken, refreshToken);
    }
}
