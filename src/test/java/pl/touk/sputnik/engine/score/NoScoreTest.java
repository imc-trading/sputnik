package pl.touk.sputnik.engine.score;

import org.junit.jupiter.api.Test;
import pl.touk.sputnik.TestEnvironment;
import pl.touk.sputnik.review.Review;

import static org.assertj.core.api.Assertions.assertThat;

class NoScoreTest extends TestEnvironment {

    @Test
    void shouldAddNoScoreToReview() {
        Review review = review();

        Score score = new NoScore().score(review);

        assertThat(score).isNull();
    }

    @Test
    public void athateh() {
        System.out.println(new Score("1", 1));
    }
}