package backend.academy.transform;

import backend.academy.point.Point;

public class LinearTransformer extends Transformer {

    @Override
    protected Point ownTransform(Point point) {
        return point;
    }
}
