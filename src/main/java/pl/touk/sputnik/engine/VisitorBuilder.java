package pl.touk.sputnik.engine;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.configuration.CliOption;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.GeneralOption;
import pl.touk.sputnik.engine.visitor.AfterReviewVisitor;
import pl.touk.sputnik.engine.visitor.BeforeReviewVisitor;
import pl.touk.sputnik.engine.visitor.FilterOutTestFilesVisitor;
import pl.touk.sputnik.engine.visitor.LimitCommentVisitor;
import pl.touk.sputnik.engine.visitor.RegexFilterFilesVisitor;
import pl.touk.sputnik.engine.visitor.SummaryMessageVisitor;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VisitorBuilder {
    @NotNull
    public List<BeforeReviewVisitor> buildBeforeReviewVisitors(Configuration configuration) {
        List<BeforeReviewVisitor> beforeReviewVisitors = new ArrayList<>();
        addTestFilesFilterIfConfigured(configuration, beforeReviewVisitors);
        addRegexFilterIfConfigured(configuration, beforeReviewVisitors);
        return beforeReviewVisitors;
    }

    private void addTestFilesFilterIfConfigured(Configuration configuration, List<BeforeReviewVisitor> beforeReviewVisitors) {
        if (!BooleanUtils.toBoolean(configuration.getProperty(GeneralOption.PROCESS_TEST_FILES))) {
            beforeReviewVisitors.add(new FilterOutTestFilesVisitor(configuration.getProperty(GeneralOption.JAVA_TEST_DIR)));
        }
    }

    private void addRegexFilterIfConfigured(Configuration configuration, List<BeforeReviewVisitor> beforeReviewVisitors) {
        String fileRegex = configuration.getProperty(CliOption.FILE_REGEX);
        if (fileRegex != null) {
            beforeReviewVisitors.add(new RegexFilterFilesVisitor(fileRegex));
        }
    }

    @NotNull
    public List<AfterReviewVisitor> buildAfterReviewVisitors(Configuration configuration) {
        List<AfterReviewVisitor> afterReviewVisitors = new ArrayList<>();

        String passingComment = configuration.getProperty(GeneralOption.MESSAGE_SCORE_PASSING_COMMENT);
        afterReviewVisitors.add(new SummaryMessageVisitor(passingComment));

        int maxNumberOfComments = NumberUtils.toInt(configuration.getProperty(GeneralOption.MAX_NUMBER_OF_COMMENTS), 0);
        if (maxNumberOfComments > 0) {
            afterReviewVisitors.add(new LimitCommentVisitor(maxNumberOfComments));
        }

        return afterReviewVisitors;
    }

}
