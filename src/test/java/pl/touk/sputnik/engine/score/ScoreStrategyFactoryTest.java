package pl.touk.sputnik.engine.score;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.ConfigurationSetup;
import pl.touk.sputnik.configuration.GeneralOption;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreStrategyFactoryTest {
        @Test
        void shouldBuildNoScoreVisitor() {
            Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                    GeneralOption.SCORE_STRATEGY.getKey(), "NOscore"
            ));

            assertThat(new ScoreStrategies().buildScoreStrategy(config))
                    .isEqualTo(new NoScore());
        }

        @Test
        void shouldBuildScoreAlwaysPassVisitor() {
            Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                    GeneralOption.SCORE_STRATEGY.getKey(), "scoreAlwaysPass",
                    GeneralOption.SCORE_PASSING_KEY.getKey(), "Verified",
                    GeneralOption.SCORE_PASSING_VALUE.getKey(), "2"
            ));

            assertThat(new ScoreStrategies().buildScoreStrategy(config))
                    .isEqualTo(new ScoreAlwaysPass(new Score("Verified", 2)));
        }

        @Test
        void shouldBuildScorePassIfEmptyVisitor() {
            Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                    GeneralOption.SCORE_STRATEGY.getKey(), "SCOREPASSIFEMPTY",
                    GeneralOption.SCORE_PASSING_KEY.getKey(), "Verified",
                    GeneralOption.SCORE_PASSING_VALUE.getKey(), "3",
                    GeneralOption.SCORE_FAILING_KEY.getKey(), "Verified",
                    GeneralOption.SCORE_FAILING_VALUE.getKey(), "-3"
            ));

            assertThat(new ScoreStrategies().buildScoreStrategy(config))
                    .isEqualTo(new ScorePassIfEmpty(new Score("Verified", 3), new Score("Verified", -3)));
        }

        @Test
        void shouldBuildScorePassIfNoErrorsVisitor() {
            Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                    GeneralOption.SCORE_STRATEGY.getKey(), "SCOREPassIfNoErrors",
                    GeneralOption.SCORE_PASSING_KEY.getKey(), "Code-Review",
                    GeneralOption.SCORE_PASSING_VALUE.getKey(), "1",
                    GeneralOption.SCORE_FAILING_KEY.getKey(), "Code-Review",
                    GeneralOption.SCORE_FAILING_VALUE.getKey(), "-2"
            ));

            assertThat(new ScoreStrategies().buildScoreStrategy(config))
                    .isEqualTo(new ScorePassIfNoErrors(new Score("Code-Review", 1), new Score("Code-Review", -2)));
        }

        @Test
        void shouldBuildDefaultScoreAlwaysPassIfStrategyIsUnknown() {
            Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                    GeneralOption.SCORE_STRATEGY.getKey(), "mySimpleStrategy"
            ));

            assertThat(new ScoreStrategies().buildScoreStrategy(config))
                    .isEqualTo(new ScoreAlwaysPass(new Score("Code-Review", 1)));
        }
}