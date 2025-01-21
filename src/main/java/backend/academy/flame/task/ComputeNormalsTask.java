package backend.academy.flame.task;

import backend.academy.pixel.Pixel;
import java.util.concurrent.RecursiveAction;
import lombok.RequiredArgsConstructor;

/**
 * Задача вычисления нормалей, способная делиться на части
 */
@RequiredArgsConstructor
public class ComputeNormalsTask extends RecursiveAction {
    private static final int THRESHOLD = 50; // Минимальное количество строк для разделения задачи
    private final Pixel[][] pixels;
    private final double[][] normals;
    private final int startRow;
    private final int endRow;

    public ComputeNormalsTask(Pixel[][] pixels, double[][] normals) {
        this(pixels, normals, 0, pixels.length);
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            // Обрабатываем строки
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    Pixel pixel = pixels[i][j];
                    if (pixel.hitCount() != 0) {
                        normals[i][j] = Math.log10(pixel.hitCount());
                    }
                }
            }
        } else {
            // Разделяем задачу
            int mid = (startRow + endRow) / 2;
            ComputeNormalsTask topTask = new ComputeNormalsTask(pixels, normals, startRow, mid);
            ComputeNormalsTask bottomTask = new ComputeNormalsTask(pixels, normals, mid, endRow);
            invokeAll(topTask, bottomTask);
        }
    }
}
