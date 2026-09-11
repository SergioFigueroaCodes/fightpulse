package com.sergiofigueroa.fightpulse.service;

import com.sergiofigueroa.fightpulse.model.CategorizedVideo;
import com.sergiofigueroa.fightpulse.model.Video;
import com.sergiofigueroa.fightpulse.model.VideoSnapshot;
import com.sergiofigueroa.fightpulse.model.VideoSnapshotResponse;
import com.sergiofigueroa.fightpulse.repository.VideoRepository;
import com.sergiofigueroa.fightpulse.repository.VideoSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class VideoPersistenceService {

    private final VideoRepository videoRepository;
    private final VideoSnapshotRepository snapshotRepository;

    public VideoPersistenceService(
            VideoRepository videoRepository,
            VideoSnapshotRepository snapshotRepository) {

        this.videoRepository = videoRepository;
        this.snapshotRepository = snapshotRepository;
    }

    @Transactional
    public int saveVideos(List<CategorizedVideo> videos) {

        if (videos == null || videos.isEmpty()) {
            return 0;
        }

        for (CategorizedVideo result : videos) {
            saveVideoAndSnapshot(result);
        }

        return videos.size();
    }

    private void saveVideoAndSnapshot(CategorizedVideo result) {

        // Find the existing video or create a new video.
        Video video = videoRepository
                .findByYoutubeVideoId(result.videoId())
                .orElseGet(() -> new Video(
                        result.videoId(),
                        result.title(),
                        result.channelTitle(),
                        parsePublishedAt(result.publishedAt()),
                        result.thumbnailUrl(),
                        result.category()
                ));

        // Update the video information in case YouTube changed it.
        video.setTitle(result.title());
        video.setChannelTitle(result.channelTitle());
        video.setPublishedAt(
                parsePublishedAt(result.publishedAt())
        );
        video.setThumbnailUrl(result.thumbnailUrl());
        video.setCategory(result.category());

        // Save the video before creating its snapshot.
        Video savedVideo = videoRepository.save(video);

        // Store a new historical statistics snapshot.
        VideoSnapshot snapshot = new VideoSnapshot(
                savedVideo,
                result.viewCount(),
                result.likeCount(),
                result.commentCount(),
                result.engagementRate(),
                Instant.now()
        );

        snapshotRepository.save(snapshot);
    }

    private Instant parsePublishedAt(String publishedAt) {

        if (publishedAt == null || publishedAt.isBlank()) {
            return null;
        }

        try {
            return Instant.parse(publishedAt);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VideoSnapshotResponse> getSnapshotsForVideo(
            String youtubeVideoId) {

        return videoRepository
                .findByYoutubeVideoId(youtubeVideoId)
                .map(video -> snapshotRepository
                        .findByVideoOrderByCollectedAtDesc(video)
                        .stream()
                        .map(this::convertToSnapshotResponse)
                        .toList())
                .orElseGet(List::of);
    }

    private VideoSnapshotResponse convertToSnapshotResponse(
            VideoSnapshot snapshot) {

        return new VideoSnapshotResponse(
                snapshot.getId(),
                snapshot.getViewCount(),
                snapshot.getLikeCount(),
                snapshot.getCommentCount(),
                snapshot.getEngagementRate(),
                snapshot.getCollectedAt()
        );
    }

    @Transactional(readOnly = true)
    public long countVideos() {
        return videoRepository.count();
    }

    @Transactional(readOnly = true)
    public long countSnapshots() {
        return snapshotRepository.count();
    }
}