package backend.academy.image.drawer;

import backend.academy.pixel.Pixels;
import java.awt.image.BufferedImage;

public interface Drawer {
    /**
     * Отрисовать отрендеренное изображение
     */
    BufferedImage draw(ImageSettings imageSettings, Pixels pixels);
}
