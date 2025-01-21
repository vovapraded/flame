package backend.academy.flame.task;

import backend.academy.flame.GammaCorrector;
import backend.academy.pixel.Pixel;
import java.util.concurrent.RecursiveAction;
import lombok.RequiredArgsConstructor;

/**
 * Задача применения гамма-коррекция, способная делиться на части
 */
@RequiredArgsConstructor
public class ApplyGammaTask extends RecursiveAction implements GammaCorrector {
    private static final int THRESHOLD = 50; // Минимальное количество строк для разделения задачи
    private final Pixel[][] pixels;
    private final double[][] normals;
    private final double gamma;
    private final double maxNormal;
    private final int startRow;
    private final int endRow;

    public ApplyGammaTask(Pixel[][] pixels, double[][] normals, double gamma, double maxNormal) {
        this(pixels, normals, gamma, maxNormal, 0, pixels.length);
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            // Обрабатываем строки
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    correct(pixels, i, j, gamma, maxNormal, normals);
                }
            }
        } else {
            // Разделяем задачу
            int mid = (startRow + endRow) / 2;
            ApplyGammaTask topTask = new ApplyGammaTask(pixels, normals, gamma, maxNormal, startRow, mid);
            ApplyGammaTask bottomTask = new ApplyGammaTask(pixels, normals, gamma, maxNormal, mid, endRow);
            invokeAll(topTask, bottomTask);
        }
    }
}
