package com.sergiofigueroa.fightpulse.model;

import java.time.Instant;

public record VideoSnapshotResponse(
        Long snapshotId,
        long viewCount,
        long likeCount,
        long commentCount,
        double engagementRate,
        Instant collectedAt
) {
}