package pl.touk.sputnik.engine.score;

import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.review.Review;

public interface ScoreStrategy {
    Score score(@NotNull Review review);
}
