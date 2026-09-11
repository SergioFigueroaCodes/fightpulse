package com.sergiofigueroa.fightpulse.model;

public record CategorizedVideo(
        String videoId,
        String title,
        String channelTitle,
        String publishedAt,
        String thumbnailUrl,
        VideoCategory category,
        long viewCount,
        long likeCount,
        long commentCount,
        double engagementRate
) {
}