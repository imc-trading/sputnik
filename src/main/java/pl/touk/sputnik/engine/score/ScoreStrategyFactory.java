package pl.touk.sputnik.engine.score;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.GeneralOption;

import static org.apache.commons.lang3.Validate.notBlank;

@Slf4j
public class ScoreStrategyFactory {
    private static final String NOSCORE = "NOSCORE";
    private static final String SCOREALWAYSPASS = "SCOREALWAYSPASS";
    private static final String SCOREPASSIFEMPTY = "SCOREPASSIFEMPTY";
    private static final String SCOREPASSIFNOERRORS = "SCOREPASSIFNOERRORS";

    @NotNull
    public ScoreStrategy buildScoreStrategy(Configuration configuration) {
        Score passingScore = new Score(
                configuration.getProperty(GeneralOption.SCORE_PASSING_KEY),
                Short.valueOf(configuration.getProperty(GeneralOption.SCORE_PASSING_VALUE))
        );
        Score failingScore = new Score(
                configuration.getProperty(GeneralOption.SCORE_FAILING_KEY),
                Short.valueOf(configuration.getProperty(GeneralOption.SCORE_FAILING_VALUE))
        );
        String scoreStrategy = configuration.getProperty(GeneralOption.SCORE_STRATEGY);
        notBlank(scoreStrategy);

        switch(scoreStrategy.toUpperCase()) {
            case NOSCORE:
                return new NoScore();

            case SCOREALWAYSPASS:
                return new ScoreAlwaysPass(passingScore);

            case SCOREPASSIFEMPTY:
                return new ScorePassIfEmpty(passingScore, failingScore);

            case SCOREPASSIFNOERRORS:
                return new ScorePassIfNoErrors(passingScore, failingScore);

            default:
                log.warn("Score strategy {} not found, using default ScoreAlwaysPass", scoreStrategy);
                return new ScoreAlwaysPass(passingScore);
        }
    }
}
