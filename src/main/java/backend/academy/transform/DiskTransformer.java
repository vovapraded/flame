package backend.academy.transform;

import backend.academy.point.Point;

public class DiskTransformer extends Transformer {
    @Override
    protected Point ownTransform(Point point) {
        double x = point.x();
        double y = point.y();
        double coef = Math.sqrt(x * x + y * y);
        if (x == 0) {
            return point;
        }
        double newX = 1 / Math.PI * Math.atan(y / x) * Math.sin(Math.PI * coef);
        double newY = 1 / Math.PI * Math.atan(y / x) * Math.cos(Math.PI * coef);
        return new Point(newX, newY);
    }
}
