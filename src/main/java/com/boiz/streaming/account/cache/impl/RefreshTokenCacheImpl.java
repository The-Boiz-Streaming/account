package com.boiz.streaming.account.cache.impl;

import com.boiz.streaming.account.cache.RefreshTokenCache;
import com.boiz.streaming.account.config.cache.RefreshTokenCacheProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class RefreshTokenCacheImpl implements RefreshTokenCache {

    private final RedissonClient redisson;
    private final RefreshTokenCacheProperties properties;

    private RMapCache<String, String> cache;

    @Autowired
    public RefreshTokenCacheImpl(final RedissonClient redisson,
                                 final RefreshTokenCacheProperties properties) {
        this.redisson = redisson;
        this.properties = properties;
    }

    @PostConstruct
    private void init() {
        cache = redisson.getMapCache(properties.topic());
    }

    @PreDestroy
    private void shutdown() {
        cache.clear();
    }

    @Override
    public void save(final String email, final String token) {
        if (redisson.isShuttingDown() || redisson.isShutdown()) {
            log.error("Failed to save: redisson is shutdown or shutting down");
            return;
        }

        cache.put(email, token, properties.convertedTtl(), properties.timeUnit());
    }

    @Override
    public boolean contains(final String email) {
        if (redisson.isShuttingDown() || redisson.isShutdown()) {
            log.error("Failed containment check: redisson is shutdown or shutting down");
            return false;
        }

        return cache.containsKey(email);
    }

    @Override
    public Optional<String> get(final String email) {
        if (redisson.isShuttingDown() || redisson.isShutdown()) {
            log.error("Failed to fetch object: redisson is shutdown or shutting down");
            return Optional.empty();
        }

        return Optional.ofNullable(cache.get(email));
    }

    @Override
    public boolean delete(final String email) {
        if (redisson.isShuttingDown() || redisson.isShutdown()) {
            log.error("Failed to delete data: redisson is shutdown or shutting down");
            return false;
        }

        return Objects.nonNull(cache.remove(email));
    }
}
