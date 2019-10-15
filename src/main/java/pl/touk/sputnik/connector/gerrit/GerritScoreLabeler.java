package pl.touk.sputnik.connector.gerrit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.touk.sputnik.engine.score.Score;

import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
@Getter
class GerritScoreLabeler {
    private final String scorePassingKey;
    private final String scoreFailingKey;
    private final short scorePassingValue;
    private final short scoreFailingValue;

    Map<String, Short> getReviewLabel(Score score) {
        switch (score) {
            case PASS:
                return Collections.singletonMap(scorePassingKey, scorePassingValue);
            case FAIL:
                return Collections.singletonMap(scoreFailingKey, scoreFailingValue);
            default:
                return Collections.emptyMap();
        }
    }
}
