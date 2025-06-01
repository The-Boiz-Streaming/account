package com.boiz.streaming.account.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "account")
@Data
@SuperBuilder
@RequiredArgsConstructor
public class Account {

    // region - fields

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    private Role role;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            schema = "account",
            name = "liked_releases",
            joinColumns = @JoinColumn(name = "account_id")
    )
    @Column(name = "release_id")
    @BatchSize(size = 100)
    private Set<UUID> likedReleases = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            schema = "account",
            name = "liked_tracks",
            joinColumns = @JoinColumn(name = "account_id")
    )
    @Column(name = "track_id")
    @BatchSize(size = 100)
    private Set<UUID> likedTracks = new HashSet<>();

    // endregion

    // region - helper methods

    public void addLikedRelease(final UUID releaseId) {
        this.likedReleases.add(releaseId);
    }

    public void removeLikedRelease(final UUID releaseId) {
        this.likedReleases.remove(releaseId);
    }

    public void addLikedTrack(final UUID trackId) {
        this.likedTracks.add(trackId);
    }

    public void removeLikedTrack(final UUID trackId) {
        this.likedTracks.remove(trackId);
    }

    // endregion

}
