package com.sergiofigueroa.fightpulse.service;

import com.sergiofigueroa.fightpulse.model.VideoCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VideoCategorizationServiceTest {

    private final VideoCategorizationService service =
            new VideoCategorizationService();

    @Test
    void shouldCategorizeHighlightVideo() {
        VideoCategory result =
                service.categorize("UFC Best Knockout Highlights");

        assertEquals(VideoCategory.FIGHT_HIGHLIGHT, result);
    }

    @Test
    void shouldCategorizeInterviewVideo() {
        VideoCategory result =
                service.categorize("Exclusive Fighter Interview");

        assertEquals(VideoCategory.INTERVIEW, result);
    }

    @Test
    void shouldCategorizeTrainingVideo() {
        VideoCategory result =
                service.categorize("MMA Training Technique Tutorial");

        assertEquals(VideoCategory.TRAINING, result);
    }

    @Test
    void shouldRequireReviewForUnknownVideo() {
        VideoCategory result =
                service.categorize("A Day in the Life of a UFC Fighter");

        assertEquals(VideoCategory.NEEDS_REVIEW, result);
    }

    // Test that UFC news videos are categorized as NEWS.
    @Test
    void shouldCategorizeUfcNewsVideo() {
        VideoCategory result = service.categorize(
                "Dana White THREATENING EMAILS from his MOTHER! Strickland DEMANDING MORE MONEY! Ilia FIGHT RUMOR!"
        );

        assertEquals(VideoCategory.NEWS, result);
    }

    // Test that "ko" inside another word does not mean knockout.
    @Test
    void shouldNotMistakeWordContainingKoForKnockout() {
        VideoCategory result = service.categorize(
                "Korean MMA Fighter Documentary"
        );

        assertEquals(VideoCategory.NEEDS_REVIEW, result);
    }
}