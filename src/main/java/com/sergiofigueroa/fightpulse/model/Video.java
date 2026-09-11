package com.sergiofigueroa.fightpulse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "videos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_video_youtube_id",
                        columnNames = "youtube_video_id"
                )
        }
)
public class Video {

    // Internal database ID.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The unique video ID provided by YouTube.
    @Column(
            name = "youtube_video_id",
            nullable = false,
            length = 30
    )
    private String youtubeVideoId;

    // The video's title.
    @Column(
            nullable = false,
            length = 500
    )
    private String title;

    // The name of the YouTube channel.
    @Column(
            name = "channel_title",
            nullable = false,
            length = 200
    )
    private String channelTitle;

    // The date and time when the video was published.
    @Column(name = "published_at")
    private Instant publishedAt;

    // The web address of the video's thumbnail.
    @Column(
            name = "thumbnail_url",
            length = 1000
    )
    private String thumbnailUrl;

    // Store the category name instead of its numeric position.
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private VideoCategory category;

    // JPA requires an empty constructor.
    protected Video() {
    }

    public Video(
            String youtubeVideoId,
            String title,
            String channelTitle,
            Instant publishedAt,
            String thumbnailUrl,
            VideoCategory category) {

        this.youtubeVideoId = youtubeVideoId;
        this.title = title;
        this.channelTitle = channelTitle;
        this.publishedAt = publishedAt;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public VideoCategory getCategory() {
        return category;
    }

    public void setCategory(VideoCategory category) {
        this.category = category;
    }
}