package com.sergiofigueroa.fightpulse.repository;

import com.sergiofigueroa.fightpulse.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    // Find a saved video using its unique YouTube video ID.
    Optional<Video> findByYoutubeVideoId(String youtubeVideoId);
}