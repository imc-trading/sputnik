package pl.touk.sputnik.connector.gerrit;

import com.google.gerrit.extensions.common.DiffInfo.ContentEntry;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Slf4j
public class FileDiffBuilder {

    @NotNull
    public FileDiff build(@NotNull String fileKey, @NotNull List<ContentEntry> content) {
        FileDiff fileDiff = new FileDiff(fileKey);
        int currentLine = 1;
        for (ContentEntry diffHunk : content) {
            if (diffHunk.skip != null) {
                currentLine += diffHunk.skip;
            }
            if (diffHunk.ab != null) {
                currentLine += diffHunk.ab.size();
            }
            if (diffHunk.b != null) {
                fileDiff.addHunk(currentLine, diffHunk.b.size());
                currentLine += diffHunk.b.size();
            }
        }
        return fileDiff;
    }
}
