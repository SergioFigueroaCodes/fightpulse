package com.sergiofigueroa.fightpulse.service;

import com.sergiofigueroa.fightpulse.model.CategorizedVideo;
import com.sergiofigueroa.fightpulse.model.VideoCategory;
import com.sergiofigueroa.fightpulse.repository.VideoRepository;
import com.sergiofigueroa.fightpulse.repository.VideoSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class VideoPersistenceServiceTest {

    @Autowired
    private VideoPersistenceService persistenceService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoSnapshotRepository snapshotRepository;

    @BeforeEach
    void clearDatabase() {

        // Delete snapshots first because they reference videos.
        snapshotRepository.deleteAll();
        videoRepository.deleteAll();
    }

    @Test
    void savesNewVideoAndSnapshot() {

        CategorizedVideo video = createTestVideo();

        int savedCount =
                persistenceService.saveVideos(List.of(video));

        assertEquals(1, savedCount);
        assertEquals(1, videoRepository.count());
        assertEquals(1, snapshotRepository.count());
    }

    @Test
    void doesNotDuplicateExistingVideo() {

        CategorizedVideo video = createTestVideo();

        persistenceService.saveVideos(List.of(video));
        persistenceService.saveVideos(List.of(video));

        // The same YouTube video should only be saved once.
        assertEquals(1, videoRepository.count());
    }

    @Test
    void createsNewSnapshotEveryTimeVideoIsCollected() {

        CategorizedVideo video = createTestVideo();

        persistenceService.saveVideos(List.of(video));
        persistenceService.saveVideos(List.of(video));

        // One video should have two historical snapshots.
        assertEquals(1, videoRepository.count());
        assertEquals(2, snapshotRepository.count());
    }

    @Test
    void savesNothingWhenListIsEmpty() {

        int savedCount =
                persistenceService.saveVideos(List.of());

        assertEquals(0, savedCount);
        assertEquals(0, videoRepository.count());
        assertEquals(0, snapshotRepository.count());
    }

    private CategorizedVideo createTestVideo() {

        return new CategorizedVideo(
                "test-video-123",
                "UFC Championship Fight Highlights",
                "FightPulse Test Channel",
                "2026-09-10T14:11:36Z",
                "https://example.com/thumbnail.jpg",
                VideoCategory.NEEDS_REVIEW,
                1000,
                100,
                20,
                12.0
        );
    }
}