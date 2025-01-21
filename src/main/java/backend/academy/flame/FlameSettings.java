package backend.academy.flame;

import backend.academy.ThreadMode;
import backend.academy.function.ListOfFunctions;
import backend.academy.pixel.Color;
import backend.academy.variation.Variation;
import java.util.List;

public record FlameSettings(int iterationCount,
                            Color background,
                            ListOfFunctions listOfFunctions,
                            List<Variation> variations,
                            int symmetry,
                            int samples,
                            ThreadMode threadMode) {
}
