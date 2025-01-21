package backend.academy.image.drawer;

import backend.academy.exception.UnexpectedException;
import backend.academy.pixel.Pixel;
import backend.academy.pixel.Pixels;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SingleThreadDrawer implements Drawer {

    @Override
    public BufferedImage draw(ImageSettings imageSettings, Pixels pixels) {
        // Определяем размеры изображения
        int width = imageSettings.width();
        int height = imageSettings.height();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


// Рисуем пиксели
        Pixel[][] pixelArray = pixels.pixels();
        for (int row = 0; row < pixels.pixels().length; row++) {
            for (int col = 0; col < pixels.pixels()[row].length; col++) {
                Pixel pixel = pixelArray[row][col];

                // Получаем цвет пикселя
                Color color = new Color(pixel.color().red(), pixel.color().green(), pixel.color().blue());

                // Преобразуем координаты пикселя в пространство изображения
                double normalizedX = (double) (col) / pixels.pixels()[row].length; // Нормализация в диапазон [0, 1]
                double normalizedY = (double) (row) / pixels.pixels().length; // Нормализация в диапазон [0, 1]

                int x = (int) Math.round(normalizedX * (width - 1)); // Масштабируем в [0, width - 1]
                int y = (int) Math.round(normalizedY * (height - 1)); // Масштабируем в [0, height - 1]

                if (x < 0 || x >= width || y < 0 || y >= height) {
                    throw new UnexpectedException("Coordinate out of array bounds: x=" + x + ", y=" + y);
                }
                // Устанавливаем цвет и рисуем точку
                image.setRGB(x, y, color.getRGB());
            }
        }
        graphics.dispose();

        return image;
    }
}
