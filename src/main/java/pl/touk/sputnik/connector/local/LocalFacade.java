package pl.touk.sputnik.connector.local;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.jetbrains.annotations.NotNull;
import pl.touk.sputnik.configuration.Configuration;
import pl.touk.sputnik.configuration.GeneralOption;
import pl.touk.sputnik.configuration.GeneralOptionNotSupportedException;
import pl.touk.sputnik.connector.ConnectorFacade;
import pl.touk.sputnik.connector.ConnectorValidator;
import pl.touk.sputnik.connector.Connectors;
import pl.touk.sputnik.connector.ReviewPublisher;
import pl.touk.sputnik.review.Comment;
import pl.touk.sputnik.review.Review;
import pl.touk.sputnik.review.ReviewFile;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
public class LocalFacade implements ConnectorFacade, ConnectorValidator, ReviewPublisher {
    private final Git git;
    private final LocalFacadeOutput output;

    @NotNull
    @Override
    public List<ReviewFile> listFiles() {
        try {
            List<DiffEntry> diffs = git.diff().call();
            return diffs.stream()
                    .filter(this::isNotDeleted)
                    .map(DiffEntry::getNewPath)
                    .map(ReviewFile::new)
                    .collect(toList());
        } catch (GitAPIException e) {
            throw new RuntimeException("Error when listing files", e);
        }
    }

    private boolean isNotDeleted(DiffEntry aDiffEntry) {
        return aDiffEntry.getChangeType() != ChangeType.DELETE;
    }

    @Override
    public void publish(@NotNull Review review) {
        long numFilesWithComments = review.getFiles().stream().filter(files -> !files.getComments().isEmpty()).count();
        if (numFilesWithComments == 0) {
            output.info("No sputnik comments");
            return;
        }

        for (String message : review.getMessages()) {
            output.warn(message);
        }

        for (ReviewFile file : review.getFiles()) {
            if (file.getComments().isEmpty()) {
                continue;
            }

            output.warn("");

            output.warn("{} comment(s) on {}", file.getComments().size(), file.getReviewFilename());
            for (Comment comment : file.getComments()) {
                output.warn("Line {}: {}", comment.getLine(), comment.getMessage());
            }
        }
    }

    @Override
    public Connectors name() {
        return Connectors.LOCAL;
    }

    @Override
    public void validate(Configuration configuration) throws GeneralOptionNotSupportedException {
        boolean commentOnlyChangedLines = Boolean.parseBoolean(configuration
                .getProperty(GeneralOption.COMMENT_ONLY_CHANGED_LINES));

        if (commentOnlyChangedLines) {
            throw new GeneralOptionNotSupportedException("This connector does not support "
                    + GeneralOption.COMMENT_ONLY_CHANGED_LINES.getKey());
        }
    }

    @Override
    public void setReview(@NotNull Review review) {
        publish(review);
    }
}
