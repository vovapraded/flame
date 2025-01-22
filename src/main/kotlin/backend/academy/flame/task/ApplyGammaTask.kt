import backend.academy.flame.GammaCorrector
import backend.academy.pixel.Pixel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Задача применения гамма-коррекции, способная делиться на части
 */
class ApplyGammaTask(
    private val pixels: Array<Array<Pixel?>>,
    private val normals: Array<DoubleArray>,
    private val gamma: Double,
    private val maxNormal: Double,
    private val startRow: Int = 0,
    private val endRow: Int = pixels.size
) : GammaCorrector {

     suspend fun compute() {
        if (endRow - startRow <= THRESHOLD) {
            // Обрабатываем строки
            for (i in startRow until endRow) {
                for (j in pixels[i].indices) {
                    correct(pixels, i, j, gamma, maxNormal, normals)
                }
            }
        } else {
            // Разделяем задачу
            val mid = (startRow + endRow) / 2
            val topTask = ApplyGammaTask(pixels, normals, gamma, maxNormal, startRow, mid)
            val bottomTask = ApplyGammaTask(pixels, normals, gamma, maxNormal, mid, endRow)
            coroutineScope {
                val job1 = launch { topTask.compute() }
                val job2 = launch { bottomTask.compute() }
                job1.join()
                job2.join()
            }
        }
    }


    companion object {
        private const val THRESHOLD = 50 // Минимальное количество строк для разделения задачи
    }
}