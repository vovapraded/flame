package backend.academy.flame;

import backend.academy.exception.UnexpectedException;
import backend.academy.flame.task.ApplyGammaTask;
import backend.academy.flame.task.ComputeNormalsTask;
import backend.academy.function.Function;
import backend.academy.function.ListOfFunctions;
import backend.academy.pixel.Pixel;
import backend.academy.pixel.Pixels;
import backend.academy.variation.Variation;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class MultiThreadFlame extends Flame {
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public MultiThreadFlame(ListOfFunctions listOfFunctions, List<Variation> variations, Pixels pixels) {
        super(listOfFunctions, variations, pixels);
    }

    @Override
    public void render(int sampleCount, int iterationPerSample, int symmetry) throws UnexpectedException {
        // Массив Future для хранения результатов задач
        Future<?>[] futures = new Future[sampleCount];

        // Запуск задач для каждой выборки
        for (int i = 0; i < sampleCount; i++) {
            futures[i] = threadPool.submit(() -> renderSample(iterationPerSample, symmetry));
        }

        // Ожидание завершения всех задач
        for (Future<?> future : futures) {
            try {
                future.get(); // Ждем завершения задачи
            } catch (InterruptedException | ExecutionException e) {
                throw new UnexpectedException(e.getMessage(), e);
            }
        }
        threadPool.shutdown();

        gammaCorrection();
    }

    @Override
    protected void updatePixel(int[] indexes, Function function) {
        var pixelRef = new AtomicReference<>(pixels.pixels()[indexes[0]][indexes[1]]);
        pixelRef.updateAndGet(currPixel -> {
            var newColor =
                currPixel.hitCount() == 0 ? function.startColor() : currPixel.color().mixWith(function.startColor());
            return new Pixel(newColor, currPixel.hitCount() + 1);
        });
        pixels.pixels()[indexes[0]][indexes[1]] = pixelRef.get();
    }

    @Override
    protected double computeNormals(double[][] normals) {
        try (var pool = ForkJoinPool.commonPool()) {
            pool.invoke(new ComputeNormalsTask(pixels.pixels(), normals));
        }
        return Arrays.stream(normals)
            .flatMapToDouble(Arrays::stream)
            .max()
            .orElse(0);

    }

    @Override
    protected void applyGammaCorrection(double[][] normals, double max) {
        try (var pool = ForkJoinPool.commonPool()) {
            pool.invoke(new ApplyGammaTask(pixels.pixels(), normals, GAMMA, max));
        }
    }

}
