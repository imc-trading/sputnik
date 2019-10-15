package pl.touk.sputnik.engine.score;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.review.Review;

@Slf4j
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ScorePassIfEmpty implements ScoreStrategy {
    private final Score passingScore;
    private final Score failingScore;

    @Override
    public Score score(@NotNull Review review) {
        if (review.getTotalViolationCount() == 0) {
            log.info("Adding passing score {} for no violation(s) found", passingScore);
            return passingScore;
        }

        log.info("Adding failing score {} for {} violations found", failingScore, review.getTotalViolationCount());
        return failingScore;
    }
}
