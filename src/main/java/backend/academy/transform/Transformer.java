package backend.academy.transform;

import backend.academy.function.Function;
import backend.academy.point.Point;
import backend.academy.transform.shift.ShiftTransformer;

public abstract class Transformer extends ShiftTransformer {
    /**
     * Применяет аффинный сдвиг, потом свою трансформацию
     */
    @Override
    public Point transform(Point point, Function function) {
        return ownTransform(super.transform(point, function));
    }

    protected abstract Point ownTransform(Point point);
}
