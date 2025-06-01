package com.boiz.streaming.account.dto;

import java.util.Set;
import java.util.UUID;

public record AccountInfo(UUID id, String email, String role, Set<UUID> likedReleases, Set<UUID> likedTracks) {
    public static AccountInfo of(final String email,
                                 final String role,
                                 final Set<UUID> likedReleases,
                                 final Set<UUID> likedTracks) {
        return new AccountInfo(null, email, role, likedReleases, likedTracks);
    }
}
