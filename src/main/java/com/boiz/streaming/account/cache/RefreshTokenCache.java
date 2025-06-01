package com.boiz.streaming.account.cache;

import java.util.Optional;

public interface RefreshTokenCache {

    void save(final String email, final String token);

    boolean contains(final String email);

    Optional<String> get(final String email);

    boolean delete(final String email);

}
