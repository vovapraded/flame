package backend.academy.flame;

import backend.academy.pixel.Color;
import backend.academy.pixel.Pixel;

/**
 * Интерфейс применения гамма-коррекции к пикселю
 */
public interface GammaCorrector {
    default void correct(Pixel[][] pixels, int i, int j, double gamma, double maxNormal, double[][] normals) {
        Pixel pixel = pixels[i][j];
        if (pixel.hitCount() != 0) {
            double coef = Math.pow(normals[i][j] / maxNormal, 1 / gamma);
            int red = (int) Math.round(pixel.color().red() * coef);
            int green = (int) Math.round(pixel.color().green() * coef);
            int blue = (int) Math.round(pixel.color().blue() * coef);
            pixels[i][j] = pixel.withColor(new Color(red, green, blue));
        }
    }

}
