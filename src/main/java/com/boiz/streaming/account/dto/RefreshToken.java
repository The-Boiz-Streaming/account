package com.boiz.streaming.account.dto;

public record RefreshToken(String refreshToken) {
    public static RefreshToken of(final String refreshToken) {
        return new RefreshToken(refreshToken);
    }
}
