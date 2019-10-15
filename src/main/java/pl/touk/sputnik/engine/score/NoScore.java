package pl.touk.sputnik.engine.score;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.review.Review;

@Slf4j
@EqualsAndHashCode
public class NoScore implements ScoreStrategy {

    @Override
    public Score score(@NotNull Review review) {
        log.info("No score for review");
        return null;
    }
}
