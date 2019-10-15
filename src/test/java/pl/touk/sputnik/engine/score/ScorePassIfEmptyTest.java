package pl.touk.sputnik.engine.score;

import org.junit.jupiter.api.Test;
import pl.touk.sputnik.review.Review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScorePassIfEmptyTest {
    private static final Score PASSING_SCORE = new Score("Sputnik-Pass", 1);
    private static final Score FAILING_SCORE = new Score("Code-Review", -2);

    private Review reviewMock = mock(Review.class);
    private static final ScorePassIfEmpty SCORE_PASS_IF_EMPTY = new ScorePassIfEmpty(PASSING_SCORE, FAILING_SCORE);

    @Test
    void shouldPassIfViolationsIsEmpty() {
        when(reviewMock.getTotalViolationCount()).thenReturn(0);

        Score score = SCORE_PASS_IF_EMPTY.score(reviewMock);

        assertThat(score).isEqualTo(PASSING_SCORE);
    }

    @Test
    void shouldFailIfViolationsIsNotEmpty() {
        when(reviewMock.getTotalViolationCount()).thenReturn(1);

        Score score = SCORE_PASS_IF_EMPTY.score(reviewMock);

        assertThat(score).isEqualTo(FAILING_SCORE);
    }

}