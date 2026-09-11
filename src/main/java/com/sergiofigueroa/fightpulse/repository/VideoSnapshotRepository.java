package com.sergiofigueroa.fightpulse.repository;

import com.sergiofigueroa.fightpulse.model.Video;
import com.sergiofigueroa.fightpulse.model.VideoSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoSnapshotRepository
        extends JpaRepository<VideoSnapshot, Long> {

    // Find all snapshots for a video and show the newest first.
    List<VideoSnapshot> findByVideoOrderByCollectedAtDesc(Video video);
}