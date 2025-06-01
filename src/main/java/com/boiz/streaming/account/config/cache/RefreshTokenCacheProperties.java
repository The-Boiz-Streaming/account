package com.boiz.streaming.account.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.boiz.streaming.account.util.Args.nonNullOrDefault;

@ConfigurationProperties(prefix = "boiz-streaming.auth.refresh-token-cache")
public record RefreshTokenCacheProperties(String topic, Duration ttl) {

    private static final String CACHE_TOPIC = "boiz-streaming:refresh-token";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    public RefreshTokenCacheProperties {
        topic = nonNullOrDefault(topic, CACHE_TOPIC);
        ttl = nonNullOrDefault(ttl, CACHE_TTL);
    }

    public TimeUnit timeUnit() {
        return TimeUnit.MILLISECONDS;
    }

    public long convertedTtl() {
        return ttl.toMillis();
    }

}
