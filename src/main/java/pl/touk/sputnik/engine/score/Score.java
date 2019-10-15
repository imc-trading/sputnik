package pl.touk.sputnik.engine.score;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Score {
    private final String label;
    private final int score;
}
