package com.sergiofigueroa.fightpulse.service;

import com.sergiofigueroa.fightpulse.model.VideoCategory;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class VideoCategorizationService {

    public VideoCategory categorize(String videoTitle) {

        // A missing or blank title cannot be categorized automatically.
        if (videoTitle == null || videoTitle.isBlank()) {
            return VideoCategory.NEEDS_REVIEW;
        }

        // Convert the title to lowercase so capitalization does not matter.
        String title = videoTitle.toLowerCase(Locale.ROOT);

        // Identify fight footage and highlight videos.
        if (containsAny(
                title,
                "highlight",
                "highlights",
                "knockout",
                "ko",
                "finish",
                "submission",
                "full fight",
                "fight moments",
                "top moments")) {

            return VideoCategory.FIGHT_HIGHLIGHT;
        }

        // Identify interviews and press-conference videos.
        if (containsAny(
                title,
                "interview",
                "reacts",
                "speaks",
                "press conference",
                "exclusive interview",
                "entrevista")) {

            return VideoCategory.INTERVIEW;
        }

        // Identify predictions, previews, and fight analysis.
        if (containsAny(
                title,
                "prediction",
                "predictions",
                "picks",
                "preview",
                "breakdown",
                "fight analysis",
                "who will win",
                "previsão",
                "palpite",
                "análise")) {

            return VideoCategory.PREDICTION;
        }

        // Identify MMA news and announcement videos.
        if (containsAny(
                title,
                "news",
                "update",
                "announced",
                "announcement",
                "breaking",
                "reveals",
                "returns",
                "ready to return",
                "notícia",
                "notícias",
                "atualização",
                "anunciado")) {

            return VideoCategory.NEWS;
        }

        // Identify training and instructional videos.
        if (containsAny(
                title,
                "training",
                "workout",
                "technique",
                "tutorial",
                "drill",
                "how to",
                "treino",
                "treinamento",
                "técnica")) {

            return VideoCategory.TRAINING;
        }

        // Unrecognized titles are marked for manual review.
        return VideoCategory.NEEDS_REVIEW;
    }

    private boolean containsAny(String title, String... keywords) {

        // Check each keyword against the lowercase video title.
        for (String keyword : keywords) {
            if (title.contains(keyword)) {
                return true;
            }
        }

        // No keywords were found.
        return false;
    }
}