package backend.academy.image.drawer;

import backend.academy.pixel.Pixel;
import backend.academy.pixel.Pixels;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MultiThreadDrawer implements Drawer {

    @Override
    public BufferedImage draw(ImageSettings imageSettings, Pixels pixels) {
        // Определяем размеры изображения
        int width = imageSettings.width();
        int height = imageSettings.height();

        // Создаем пустое изображение
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Создаем ForkJoinPool и запускаем главную задачу
        try (var pool = ForkJoinPool.commonPool()) {
            pool.invoke(new DrawTask(0, pixels.pixels().length, image, pixels, width, height));
        }
        return image;
    }

    /**
     * Рекурсивная задача для отрисовки
     */
    private static class DrawTask extends RecursiveTask<Void> {
        private static final int THRESHOLD = 50; // Максимальное количество строк для одной задачи

        private final int startRow; // Начальная строка
        private final int endRow;   // Конечная строка (не включительно)
        private final BufferedImage image;
        private final Pixels pixels;
        private final int width;
        private final int height;

        DrawTask(int startRow, int endRow, BufferedImage image, Pixels pixels, int width, int height) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.image = image;
            this.pixels = pixels;
            this.width = width;
            this.height = height;
        }

        @Override
        protected Void compute() {
            // Если количество строк меньше порога, выполняем задачу напрямую
            if (endRow - startRow <= THRESHOLD) {
                drawPixels();
                return null;
            }

            // Делим задачу на две подзадачи
            int mid = (startRow + endRow) / 2;
            DrawTask topTask = new DrawTask(startRow, mid, image, pixels, width, height);
            DrawTask bottomTask = new DrawTask(mid, endRow, image, pixels, width, height);

            // Запускаем подзадачи параллельно и дожидаемся их завершения
            invokeAll(topTask, bottomTask);
            return null;
        }

        // Метод для непосредственной отрисовки пикселей
        private void drawPixels() {
            Pixel[][] pixelArray = pixels.pixels();

            for (int row = startRow; row < endRow; row++) {
                for (int col = 0; col < pixelArray[row].length; col++) {
                    Pixel pixel = pixelArray[row][col];

                    Color color = new Color(pixel.color().red(), pixel.color().green(), pixel.color().blue());

                    // Преобразуем координаты пикселя в пространство изображения
                    double normalizedX = (double) col / pixelArray[row].length; // Нормализация в диапазон [0, 1]
                    double normalizedY = (double) row / pixelArray.length;      // Нормализация в диапазон [0, 1]

                    int x = (int) Math.round(normalizedX * (width - 1)); // Масштабируем в [0, width - 1]
                    int y = (int) Math.round(normalizedY * (height - 1)); // Масштабируем в [0, height - 1]

                    // Устанавливаем цвет пикселя в изображении
                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        image.setRGB(x, y, color.getRGB());

                    }
                }
            }
        }
    }
}
