package pl.touk.sputnik.engine.score;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.Severity;

import java.util.Set;

public enum ScoreStrategy {
    NO_SCORE {
        @Override
        public Score score(@NotNull Review review) {
            return Score.NONE;
        }
    },

    ALWAYS_PASS {
        @Override
        public Score score(@NotNull Review review) {
            return Score.PASS;
        }
    },

    PASS_IF_EMPTY {
        @Override
        public Score score(@NotNull Review review) {
            if (review.getTotalViolationCount() == 0) {
                return Score.PASS;
            }

            return Score.FAIL;
        }
    },

    PASS_IF_NO_ERRORS {
        @Override
        public Score score(@NotNull Review review) {
            Integer errorCount = review.getViolationCount().get(Severity.ERROR);
            if (errorCount == null || errorCount == 0) {
                return Score.PASS;
            }

            return Score.FAIL;
        }
    };


    private static final String NOSCORE = "NOSCORE";
    private static final String SCOREALWAYSPASS = "SCOREALWAYSPASS";
    private static final String SCOREPASSIFEMPTY = "SCOREPASSIFEMPTY";
    private static final String SCOREPASSIFNOERRORS = "SCOREPASSIFNOERRORS";

    private static final Set<String> SCORE_STRATEGY_KEYS = ImmutableSet.of(
            NOSCORE, SCOREALWAYSPASS, SCOREPASSIFEMPTY, SCOREPASSIFNOERRORS);

    public static boolean isValidScoreStrategy(String strategy) {
        return SCORE_STRATEGY_KEYS.contains(strategy);
    }

    @NotNull
    public static ScoreStrategy of(String strategy) {
        switch(strategy) {
            case NOSCORE:
                return ScoreStrategy.NO_SCORE;

            case SCOREALWAYSPASS:
                return ScoreStrategy.ALWAYS_PASS;

            case SCOREPASSIFEMPTY:
                return ScoreStrategy.PASS_IF_EMPTY;

            case SCOREPASSIFNOERRORS:
                return ScoreStrategy.PASS_IF_NO_ERRORS;

            default:
                return ScoreStrategy.ALWAYS_PASS;
        }
    }

    abstract public Score score(@NotNull Review review);
}
