package backend.academy.transform;

import backend.academy.point.Point;

public class HeartTransformer extends Transformer {
    @Override
    protected Point ownTransform(Point point) {
        var x = point.x();
        var y = point.y();
        if (x == 0) {
            return point;
        }
        var coef = Math.sqrt(x * x + y * y);
        var newX = coef * Math.sin(coef * Math.atan(y / x));
        var newY = -coef * Math.cos(coef * Math.atan(y / x));
        return new Point(newX, newY);
    }
}
