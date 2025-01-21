package backend.academy.transform.shift;

import backend.academy.function.Function;
import backend.academy.point.Point;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ShiftTransformer {
    /**
     * Применяет аффинный сдвиг
     */
    public Point transform(Point point, Function function) {
        var coefficient = function.coefficient();
        var x = coefficient.a() * point.x() + coefficient.b() * point.y() + coefficient.c();
        var y = coefficient.d() * point.x() + coefficient.e() * point.y() + coefficient.f();
        return new Point(x, y);
    }

}
