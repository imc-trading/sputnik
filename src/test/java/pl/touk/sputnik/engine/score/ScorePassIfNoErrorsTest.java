package pl.touk.sputnik.engine.score;

import org.junit.jupiter.api.Test;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.Severity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScorePassIfNoErrorsTest {
    private static final Score PASSING_SCORE = new Score("Sputnik-Pass", 1);
    private static final Score FAILING_SCORE = new Score("Code-Review", -2);

    private static final ScorePassIfNoErrors SCORE_PASS_IF_EMPTY = new ScorePassIfNoErrors(PASSING_SCORE, FAILING_SCORE);
    private Review reviewMock = mock(Review.class, RETURNS_DEEP_STUBS);

    @Test
    void shouldPassIfErrorCountIsNull() {
        when(reviewMock.getViolationCount().get(Severity.ERROR)).thenReturn(null);

        Score score = SCORE_PASS_IF_EMPTY.score(reviewMock);

        assertThat(score).isEqualTo(PASSING_SCORE);
    }

    @Test
    void shouldPassIfErrorCountIsZero() {
        when(reviewMock.getViolationCount().get(Severity.ERROR)).thenReturn(0);

        Score score = SCORE_PASS_IF_EMPTY.score(reviewMock);

        assertThat(score).isEqualTo(PASSING_SCORE);
    }

    @Test
    void shouldFailIfErrorCountIsNotZero() {
        when(reviewMock.getViolationCount().get(Severity.ERROR)).thenReturn(1);

        Score score = SCORE_PASS_IF_EMPTY.score(reviewMock);

    assertThat(score).isEqualTo(FAILING_SCORE);
    }

}