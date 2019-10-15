package pl.touk.sputnik.engine.score;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.GeneralOption;

import static org.apache.commons.lang3.Validate.notBlank;

@Slf4j
public class ScoreStrategies {
    private static final String NOSCORE = "NOSCORE";
    private static final String SCOREALWAYSPASS = "SCOREALWAYSPASS";
    private static final String SCOREPASSIFEMPTY = "SCOREPASSIFEMPTY";
    private static final String SCOREPASSIFNOERRORS = "SCOREPASSIFNOERRORS";

    @NotNull
    public ScoreStrategy buildScoreStrategy(Configuration configuration) {
        String scoreStrategy = configuration.getProperty(GeneralOption.SCORE_STRATEGY);
        notBlank(scoreStrategy);

        String myS = scoreStrategy.toUpperCase();
        return getScoreStrategy(scoreStrategy, myS);
    }

    @NotNull
    private ScoreStrategy getScoreStrategy(String aScoreStrategy, String aS) {
        switch(aS) {
            case NOSCORE:
                return ScoreStrategy.NO_SCORE;

            case SCOREALWAYSPASS:
                return ScoreStrategy.ALWAYS_PASS;

            case SCOREPASSIFEMPTY:
                return ScoreStrategy.PASS_IF_EMPTY;

            case SCOREPASSIFNOERRORS:
                return ScoreStrategy.PASS_IF_NO_ERRORS;

            default:
                log.warn("Score strategy {} not found, using default ScoreAlwaysPass", aScoreStrategy);
                return ScoreStrategy.ALWAYS_PASS;
        }
    }
}
