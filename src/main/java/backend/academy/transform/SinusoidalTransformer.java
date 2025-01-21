package backend.academy.transform;

import backend.academy.point.Point;

public class SinusoidalTransformer extends Transformer {

    @Override
    protected Point ownTransform(Point point) {
        return new Point((int) Math.round(Math.sin(point.x())), (int) Math.round(Math.sin(point.y()))
        );
    }
}
