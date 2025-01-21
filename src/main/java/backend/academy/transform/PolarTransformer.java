package backend.academy.transform;

import backend.academy.point.Point;

public class PolarTransformer extends Transformer {
    @Override
    protected Point ownTransform(Point point) {
        if (point.x() == 0) {
            return point;
        }
        var newX = Math.atan(point.y() / point.x());
        var newY = Math.sqrt(Math.pow(point.x(), 2) + Math.pow(point.y(), 2)) - 1;
        return new Point(newX, newY);
    }
}
