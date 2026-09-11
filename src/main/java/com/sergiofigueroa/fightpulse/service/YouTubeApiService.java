package com.sergiofigueroa.fightpulse.service;

import com.sergiofigueroa.fightpulse.model.YouTubeSearchResponse;
import com.sergiofigueroa.fightpulse.model.YouTubeStatisticsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class YouTubeApiService {

    private final RestClient restClient;
    private final String apiKey;

    public YouTubeApiService(@Value("${youtube.api.key}") String apiKey) {

        // Create the HTTP client using YouTube's base API address.
        this.restClient = RestClient.builder()
                .baseUrl("https://www.googleapis.com/youtube/v3")
                .build();

        this.apiKey = apiKey;
    }

    public YouTubeSearchResponse searchMmaVideos(String searchTerm) {

        // Search YouTube and retrieve basic video information.
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", searchTerm)
                        .queryParam("type", "video")
                        .queryParam("maxResults", 5)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(YouTubeSearchResponse.class);
    }

    public YouTubeStatisticsResponse getVideoStatistics(
            List<String> videoIds) {

        // Avoid sending an unnecessary request when there are no IDs.
        if (videoIds == null || videoIds.isEmpty()) {
            return new YouTubeStatisticsResponse(List.of());
        }

        // The YouTube API accepts multiple IDs separated by commas.
        String joinedVideoIds = String.join(",", videoIds);

        // Retrieve views, likes, and comments for all selected videos.
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "statistics")
                        .queryParam("id", joinedVideoIds)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(YouTubeStatisticsResponse.class);
    }
}