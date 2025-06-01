package com.boiz.streaming.account.exception;

public class UnsupportedRoleException extends RuntimeException {

    private static final String UNSUPPORTED_ROLE = "unsupported.role";

    public static UnsupportedRoleException of() {
        return new UnsupportedRoleException(UNSUPPORTED_ROLE);
    }

    private UnsupportedRoleException(final String message) {
        super(message);
    }
}
