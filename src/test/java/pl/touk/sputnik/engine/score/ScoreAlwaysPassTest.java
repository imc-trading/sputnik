package pl.touk.sputnik.engine.score;

import org.junit.jupiter.api.Test;
import pl.touk.sputnik.TestEnvironment;
import pl.touk.sputnik.review.Review;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreAlwaysPassTest extends TestEnvironment {

    @Test
    void shouldAddScoreToReview() {
        Review review = review();

        Score myPassingScore = new Score("Sputnik-Pass", 1);
        Score myScore = new ScoreAlwaysPass(myPassingScore).score(review);

        assertThat(myScore).isEqualTo(myPassingScore);
    }

}