package pl.touk.sputnik.engine;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import pl.touk.sputnik.configuration.CliOption;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.ConfigurationSetup;
import pl.touk.sputnik.configuration.GeneralOption;
import pl.touk.sputnik.engine.visitor.FilterOutTestFilesVisitor;
import pl.touk.sputnik.engine.visitor.LimitCommentVisitor;
import pl.touk.sputnik.engine.visitor.RegexFilterFilesVisitor;
import pl.touk.sputnik.engine.visitor.SummaryMessageVisitor;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class VisitorBuilderTest {

    @Test
    void shouldNotBuildBeforeVisitors() {
        Configuration config = new ConfigurationSetup().setUp(Collections.<String, String>emptyMap());

        assertThat(new VisitorBuilder().buildBeforeReviewVisitors(config)).isEmpty();
    }

    @Test
    void shouldNotBuildDisabledBeforeVisitors() {
        Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                GeneralOption.PROCESS_TEST_FILES.getKey(), "true"
        ));

        assertThat(new VisitorBuilder().buildBeforeReviewVisitors(config)).isEmpty();
    }

    @Test
    void shouldBuildBeforeVisitors() {
        Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                GeneralOption.PROCESS_TEST_FILES.getKey(), "false"
        ));

        assertThat(new VisitorBuilder().buildBeforeReviewVisitors(config))
                .hasSize(1)
                .extracting("class")
                .containsExactly(FilterOutTestFilesVisitor.class);
    }

    @Test
    void shouldAddRegexFilterToBeforeVisitorsWhenConfigured() {
        Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
            CliOption.FILE_REGEX.getKey(), "^myModule/.+"
        ));

        assertThat(new VisitorBuilder().buildBeforeReviewVisitors(config))
            .hasSize(1)
            .extracting("class")
            .containsExactly(RegexFilterFilesVisitor.class);
    }

    @Test
    void shouldBuildAfterVisitors() {
        Configuration config = new ConfigurationSetup().setUp(Collections.<String, String>emptyMap());

        assertThat(new VisitorBuilder().buildAfterReviewVisitors(config))
                .hasSize(1)
                .extracting("class")
                .containsExactly(SummaryMessageVisitor.class);
    }

    @Test
    void shouldNotBuildDisabledAfterVisitors() {
        Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                GeneralOption.MAX_NUMBER_OF_COMMENTS.getKey(), "0"
        ));

        assertThat(new VisitorBuilder().buildAfterReviewVisitors(config))
                .hasSize(1)
                .extracting("class")
                .containsExactly(SummaryMessageVisitor.class);
    }

    @Test
    void shouldBuildFilterOutCommentVisitor() {
        Configuration config = new ConfigurationSetup().setUp(ImmutableMap.of(
                GeneralOption.MAX_NUMBER_OF_COMMENTS.getKey(), "50"
        ));

        assertThat(new VisitorBuilder().buildAfterReviewVisitors(config))
                .hasSize(2)
                .extracting("class")
                .containsExactly(SummaryMessageVisitor.class, LimitCommentVisitor.class);
    }

}