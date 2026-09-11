package com.sergiofigueroa.fightpulse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YouTubeSearchResponse(
        List<SearchItem> items
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SearchItem(
            VideoId id,
            Snippet snippet
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VideoId(
            String videoId
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Snippet(
            String publishedAt,
            String title,
            String description,
            String channelTitle,
            Thumbnails thumbnails
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Thumbnails(
            Thumbnail medium
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Thumbnail(
            String url,
            Integer width,
            Integer height
    ) {
    }
}