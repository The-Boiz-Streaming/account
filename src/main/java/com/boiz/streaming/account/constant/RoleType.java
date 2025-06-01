package com.boiz.streaming.account.constant;

import com.boiz.streaming.account.exception.UnsupportedRoleException;

public enum RoleType {

    ADMIN,
    MANAGER,
    USER;

    public static RoleType of(final String role) {
        return switch (role) {
            case "USER", "ROLE_USER" -> USER;
            case "MANAGER", "ROLE_MANAGER" -> MANAGER;
            case "ADMIN", "ROLE_ADMIN" -> ADMIN;
            default -> throw UnsupportedRoleException.of();
        };
    }

}
