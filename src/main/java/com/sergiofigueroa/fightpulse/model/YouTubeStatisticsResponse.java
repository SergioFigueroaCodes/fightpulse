package com.sergiofigueroa.fightpulse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YouTubeStatisticsResponse(
        List<StatisticsItem> items
) {

    // Represents one video returned by the YouTube videos endpoint.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StatisticsItem(
            String id,
            Statistics statistics
    ) {
    }

    // YouTube returns these numbers as strings.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Statistics(
            String viewCount,
            String likeCount,
            String commentCount
    ) {
    }
}