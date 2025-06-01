package com.boiz.streaming.account.messages;

import java.util.UUID;

public record LikedRelease(UUID accountId, UUID likedRelease) {
}
