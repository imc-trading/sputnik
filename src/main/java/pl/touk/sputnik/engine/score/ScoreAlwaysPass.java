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
public class ScoreAlwaysPass implements ScoreStrategy {
    private final Score passingScore;

    @Override
    public Score score(@NotNull Review review) {
        log.info("Adding static passing score {} to review", passingScore);
        return passingScore;
    }
}
