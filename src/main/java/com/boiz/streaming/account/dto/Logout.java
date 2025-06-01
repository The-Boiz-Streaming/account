package com.boiz.streaming.account.dto;

import jakarta.servlet.http.HttpServletResponse;

public record Logout(HttpServletResponse response) {
    public static Logout of(HttpServletResponse response) {
        return new Logout(response);
    }
}
