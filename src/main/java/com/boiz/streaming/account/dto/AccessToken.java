package com.boiz.streaming.account.dto;

public record AccessToken(String accessToken) {
    public static AccessToken of(final String accessToken) {
        return new AccessToken(accessToken);
    }
}
