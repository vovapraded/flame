package backend.academy.pixel;

import backend.academy.exception.IllegalArgumentException;
import lombok.Getter;

@Getter
public class Pixels {
    @Getter
    private static final double MIN_X = -4.0;
    @Getter
    private static final double MAX_X = 4.0;
    @Getter
    private static final double MIN_Y = -4.0;
    @Getter
    private static final double MAX_Y = 4.0;
    private final Pixel[][] pixels;
    private final double scaleX;
    private final double scaleY;

    public Pixels(int width, int height, Color defaultColor) {
        this.pixels = new Pixel[height][width];
        this.scaleY = height / (MAX_Y - MIN_Y);
        this.scaleX = width / (MAX_X - MIN_X);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = new Pixel(defaultColor, 0);
            }
        }
    }

    public Pixels(Pixel[][] pixels, double scaleX, double scaleY) {
        this.pixels = pixels;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public int[] getPixelIndexes(double x, double y) {
        if (x < MIN_X || x > MAX_X || y < MIN_Y || y > MAX_Y) {
            throw new IllegalArgumentException("Coordinates are out of bounds: (" + x + ", " + y + ")");
        }
        return new int[] {(int) Math.round((y - MIN_Y) * scaleY), (int) Math.round(((x - MIN_X) * scaleX))};
    }
}
