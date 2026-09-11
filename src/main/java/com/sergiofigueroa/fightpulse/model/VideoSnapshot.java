package com.sergiofigueroa.fightpulse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "video_snapshots")
public class VideoSnapshot {

    // Internal database ID for this snapshot.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The video connected to these statistics.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "video_id",
            nullable = false
    )
    private Video video;

    // Number of views when this snapshot was collected.
    @Column(
            name = "view_count",
            nullable = false
    )
    private long viewCount;

    // Number of likes when this snapshot was collected.
    @Column(
            name = "like_count",
            nullable = false
    )
    private long likeCount;

    // Number of comments when this snapshot was collected.
    @Column(
            name = "comment_count",
            nullable = false
    )
    private long commentCount;

    // Engagement percentage at collection time.
    @Column(
            name = "engagement_rate",
            nullable = false
    )
    private double engagementRate;

    // Date and time when the statistics were collected.
    @Column(
            name = "collected_at",
            nullable = false
    )
    private Instant collectedAt;

    // JPA requires an empty constructor.
    protected VideoSnapshot() {
    }

    public VideoSnapshot(
            Video video,
            long viewCount,
            long likeCount,
            long commentCount,
            double engagementRate,
            Instant collectedAt) {

        this.video = video;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.engagementRate = engagementRate;
        this.collectedAt = collectedAt;
    }

    public Long getId() {
        return id;
    }

    public Video getVideo() {
        return video;
    }

    public long getViewCount() {
        return viewCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public double getEngagementRate() {
        return engagementRate;
    }

    public Instant getCollectedAt() {
        return collectedAt;
    }
}