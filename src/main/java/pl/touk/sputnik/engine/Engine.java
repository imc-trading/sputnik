package pl.touk.sputnik.engine;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.GeneralOption;
import pl.touk.sputnik.connector.ConnectorFacade;
import pl.touk.sputnik.connector.ReviewPublisher;
import pl.touk.sputnik.engine.score.Score;
import pl.touk.sputnik.engine.score.ScoreStrategy;
import pl.touk.sputnik.engine.visitor.AfterReviewVisitor;
import pl.touk.sputnik.engine.visitor.BeforeReviewVisitor;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewFile;
import pl.touk.sputnik.review.ReviewFormatterFactory;
import pl.touk.sputnik.review.ReviewProcessor;
import pl.touk.sputnik.review.Severity;

import java.util.List;

import static org.apache.commons.lang3.Validate.notBlank;

@Slf4j
public class Engine {

    private final ConnectorFacade facade;
    private final ReviewPublisher reviewPublisher;
    private final Configuration config;

    public Engine(ConnectorFacade facade, ReviewPublisher reviewPublisher, Configuration configuration) {
        this.facade = facade;
        this.reviewPublisher = reviewPublisher;
        this.config = configuration;
    }

    public Score run() {
        List<ReviewFile> reviewFiles = facade.listFiles();
        log.debug(reviewFiles.toString());
        Review review = new Review(reviewFiles, ReviewFormatterFactory.get(config));

        for (BeforeReviewVisitor beforeReviewVisitor : new VisitorBuilder().buildBeforeReviewVisitors(config)) {
            beforeReviewVisitor.beforeReview(review);
        }

        List<ReviewProcessor> processors = ProcessorBuilder.buildProcessors(config);
        ReviewRunner reviewRunner = new ReviewRunner(review);
        for (ReviewProcessor processor : processors) {
            reviewRunner.review(processor);
        }

        for (AfterReviewVisitor afterReviewVisitor : new VisitorBuilder().buildAfterReviewVisitors(config)) {
            afterReviewVisitor.afterReview(review);
        }

        String scoreStrategyName = scoreStrategyName();
        if (!ScoreStrategy.isValidScoreStrategy(scoreStrategyName)) {
            log.warn("Score strategy {} not found, using default ScoreAlwaysPass", scoreStrategyName);
        }

        Score score = ScoreStrategy.of(scoreStrategyName).score(review);
        review.setScore(score);
        log.info("{} violations found, {} errors. Adding score {}",
                review.getTotalViolationCount(),
                review.getViolationCount().get(Severity.ERROR),
                score);

        reviewPublisher.publish(review);

        return score;
    }

    @NotNull
    private String scoreStrategyName() {
        String scoreStrategyName = config.getProperty(GeneralOption.SCORE_STRATEGY);
        notBlank(scoreStrategyName);
        scoreStrategyName = scoreStrategyName.toUpperCase();
        return scoreStrategyName;
    }
}
