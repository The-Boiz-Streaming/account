package com.boiz.streaming.account.messages;

import java.util.UUID;

public record LikedTrack(UUID accountId, UUID trackId) {
}
