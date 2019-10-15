package pl.touk.sputnik.engine.score;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.Severity;

@Slf4j
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ScorePassIfNoErrors implements ScoreStrategy {
    private final Score passingScore;
    private final Score failingScore;

    @Override
    public Score score(@NotNull Review review) {
        Integer errorCount = review.getViolationCount().get(Severity.ERROR);
        if (errorCount == null || errorCount == 0) {
            log.info("Adding passing score {} for no errors found", passingScore);
            return passingScore;
        }

        log.info("Adding failing score {} for {} errors found", failingScore, errorCount);
        return failingScore;
    }
}
