package com.sergiofigueroa.fightpulse.controller;

import com.sergiofigueroa.fightpulse.model.CategorizedVideo;
import com.sergiofigueroa.fightpulse.model.Video;
import com.sergiofigueroa.fightpulse.model.VideoSnapshotResponse;
import com.sergiofigueroa.fightpulse.model.YouTubeSearchResponse;
import com.sergiofigueroa.fightpulse.model.YouTubeStatisticsResponse;
import com.sergiofigueroa.fightpulse.service.VideoCategorizationService;
import com.sergiofigueroa.fightpulse.service.VideoPersistenceService;
import com.sergiofigueroa.fightpulse.service.YouTubeApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
public class YouTubeController {

    private final YouTubeApiService youTubeApiService;
    private final VideoCategorizationService categorizationService;
    private final VideoPersistenceService persistenceService;

    public YouTubeController(
            YouTubeApiService youTubeApiService,
            VideoCategorizationService categorizationService,
            VideoPersistenceService persistenceService) {

        this.youTubeApiService = youTubeApiService;
        this.categorizationService = categorizationService;
        this.persistenceService = persistenceService;
    }

    @GetMapping("/search")
    public List<CategorizedVideo> searchVideos(
            @RequestParam(defaultValue = "MMA") String query) {

        YouTubeSearchResponse searchResponse =
                youTubeApiService.searchMmaVideos(query);

        if (searchResponse == null || searchResponse.items() == null) {
            return List.of();
        }

        List<String> videoIds = searchResponse.items()
                .stream()
                .map(item -> item.id().videoId())
                .toList();

        YouTubeStatisticsResponse statisticsResponse =
                youTubeApiService.getVideoStatistics(videoIds);

        Map<String, YouTubeStatisticsResponse.Statistics> statisticsById =
                new HashMap<>();

        if (statisticsResponse != null
                && statisticsResponse.items() != null) {

            for (YouTubeStatisticsResponse.StatisticsItem item
                    : statisticsResponse.items()) {

                statisticsById.put(
                        item.id(),
                        item.statistics()
                );
            }
        }

        return searchResponse.items()
                .stream()
                .map(item -> convertToCategorizedVideo(
                        item,
                        statisticsById.get(item.id().videoId())
                ))
                .toList();
    }

    @PostMapping("/collect")
    public Map<String, Object> collectVideos(
            @RequestParam(defaultValue = "MMA") String query) {

        List<CategorizedVideo> videos = searchVideos(query);

        int savedCount =
                persistenceService.saveVideos(videos);

        return Map.of(
                "message",
                "YouTube videos collected successfully",
                "query",
                query,
                "savedCount",
                savedCount
        );
    }

    @GetMapping("/saved")
    public List<Video> getSavedVideos() {
        return persistenceService.getAllVideos();
    }

    @GetMapping("/saved/{youtubeVideoId}/snapshots")
    public List<VideoSnapshotResponse> getVideoSnapshots(
            @PathVariable String youtubeVideoId) {

        return persistenceService
                .getSnapshotsForVideo(youtubeVideoId);
    }

    @GetMapping("/saved/count")
    public Map<String, Long> getSavedRecordCounts() {

        return Map.of(
                "videos",
                persistenceService.countVideos(),
                "snapshots",
                persistenceService.countSnapshots()
        );
    }

    private CategorizedVideo convertToCategorizedVideo(
            YouTubeSearchResponse.SearchItem item,
            YouTubeStatisticsResponse.Statistics statistics) {

        String thumbnailUrl = null;

        if (item.snippet().thumbnails() != null
                && item.snippet().thumbnails().medium() != null) {

            thumbnailUrl =
                    item.snippet().thumbnails().medium().url();
        }

        long views = 0;
        long likes = 0;
        long comments = 0;

        if (statistics != null) {
            views = parseCount(statistics.viewCount());
            likes = parseCount(statistics.likeCount());
            comments = parseCount(statistics.commentCount());
        }

        double engagementRate =
                calculateEngagementRate(
                        views,
                        likes,
                        comments
                );

        return new CategorizedVideo(
                item.id().videoId(),
                item.snippet().title(),
                item.snippet().channelTitle(),
                item.snippet().publishedAt(),
                thumbnailUrl,
                categorizationService.categorize(
                        item.snippet().title()
                ),
                views,
                likes,
                comments,
                engagementRate
        );
    }

    private long parseCount(String count) {

        if (count == null || count.isBlank()) {
            return 0;
        }

        try {
            return Long.parseLong(count);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private double calculateEngagementRate(
            long views,
            long likes,
            long comments) {

        if (views == 0) {
            return 0.0;
        }

        double engagementRate =
                ((double) (likes + comments) / views) * 100;

        return Math.round(engagementRate * 100.0) / 100.0;
    }
}