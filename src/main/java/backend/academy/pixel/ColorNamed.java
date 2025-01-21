package backend.academy.pixel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorNamed {
    BLACK(new Color(0, 0, 0)),
    WHITE(new Color(255, 255, 255)),
    RED(new Color(255, 0, 0)),
    GREEN(new Color(0, 255, 0)),
    BLUE(new Color(0, 0, 255)),
    YELLOW(new Color(255, 255, 0)),
    ORANGE(new Color(255, 0, 255));
    private final Color color;
}
