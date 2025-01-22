package backend.academy.flame;

import backend.academy.exception.IllegalArgumentException;
import backend.academy.function.Function;
import backend.academy.function.ListOfFunctions;
import backend.academy.pixel.Pixels;
import backend.academy.point.Point;
import backend.academy.variation.Variation;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Flame {
    protected static final int SKIP_ITERATIONS_COUNT = 20;
    protected static final double GAMMA = 2.2;
    protected final ListOfFunctions listOfFunctions;
    protected final List<Variation> variations;
    protected final Pixels pixels;
    public Flame(ListOfFunctions listOfFunctions, List<Variation> variations, Pixels pixels) {
        this.listOfFunctions = listOfFunctions;
        this.variations = variations;
        this.pixels = pixels;
    }

    /**
     * Отрендерить фрактал
     */
    public abstract void render(int sampleCount, int iterationPerSample, int symmetry);

    /**
     * Обновить hitCount и color пикселя
     *
     * @param indexes  индексы массива pixels
     * @param function функция, которую применили
     */
    protected abstract void updatePixel(int[] indexes, Function function);

    /**
     * Гамма-коррекция
     */
    protected void gammaCorrection() {
        var normals = new double[pixels.pixels().length][pixels.pixels()[0].length];
        var max = computeNormals(normals);
        if (max != 0) {
            applyGammaCorrection(normals, max);

        }
    }

    /**
     * Вычислить нормали всех пикселей
     *
     * @param normals массив куда записать результат
     * @return maxNormal
     */
    protected abstract double computeNormals(double[][] normals);

    /**
     * Применить гамма-коррекцию
     *
     * @param normals массив нормалей
     * @param max     максимальная нормаль
     */
    protected abstract void applyGammaCorrection(double[][] normals, double max);

    /**
     * Повернуть точку на угол theta
     *
     * @param indexes индексы точки
     * @param theta   угол поворота
     * @return новые индексы точки
     */
    protected int[] rotatePoint(int[] indexes, double theta, int width, int height) {
        // Индексы пикселей (x, y)
        double x = indexes[0];
        double y = indexes[1];

        // Находим центр изображения
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        // Перемещаем точку так, чтобы центр был в (0,0)
        double translatedX = x - centerX;
        double translatedY = y - centerY;

        // Поворот вокруг (0, 0)
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        // Новые координаты после поворота
        double rotatedX = translatedX * cosTheta - translatedY * sinTheta;
        double rotatedY = translatedX * sinTheta + translatedY * cosTheta;

        // Возвращаем точку в исходное положение относительно центра
        double newX = rotatedX + centerX;
        double newY = rotatedY + centerY;

        // Возвращаем новые индексы
        return new int[]{(int) Math.round(newX), (int) Math.round(newY)};
    }

    /**
     * Применить трансформацию к точке
     */
    protected Point updateCoords(Point point, Function function) throws IllegalArgumentException {
        var x = 0.0;
        var y = 0.0;
        for (var variation : variations) {
            var newPoint = variation.transformer().transform(point, function);
            x += variation.weight() * newPoint.x();
            y += variation.weight() * newPoint.y();
        }
        return new Point(x, y);
    }

    protected void renderSample(int iteration, int symmetry) {
        var random = ThreadLocalRandom.current();
        var startPoint =
                new Point(random.nextDouble(Pixels.MIN_X(), Pixels.MAX_X()),
                        random.nextDouble(Pixels.MIN_Y(), Pixels.MAX_Y()));
        for (int i = 0; i < SKIP_ITERATIONS_COUNT; i++) {
            var function = listOfFunctions.getRandomFunction(random);
            startPoint = updateCoords(startPoint, function);
        }
        var currentPoint = startPoint;
        int height = pixels.pixels().length;
        int width = pixels.pixels()[0].length;

        for (int i = 0; i < iteration; i++) {
            var function = listOfFunctions.getRandomFunction(random);
            currentPoint = updateCoords(currentPoint, function);
            try {
                double theta = 0.0;
                for (int part = 0; part < symmetry; theta += Math.PI * 2 / symmetry, ++part) {
                    var indexes = pixels.getPixelIndexes(currentPoint.x(), currentPoint.y());
                    indexes = rotatePoint(indexes, theta, width, height);
                    if (indexes[1] < width && indexes[0] < height && indexes[0] >= 0 && indexes[1] >= 0) {
                        updatePixel(indexes, function);
                    }
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
