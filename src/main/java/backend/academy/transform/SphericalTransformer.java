package backend.academy.transform;

import backend.academy.point.Point;

public class SphericalTransformer extends Transformer {
    @Override
    protected Point ownTransform(Point point) {
        var denominator = Math.pow(point.x(), 2) + Math.pow(point.y(), 2);
        if (denominator == 0) {
            return point;
        }
        return new Point(point.x() / denominator, point.y() / denominator);
    }
}
