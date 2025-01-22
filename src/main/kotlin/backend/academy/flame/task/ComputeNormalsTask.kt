package backend.academy.backend.academy.flame.task

import backend.academy.pixel.Pixel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.log10

/**
 * Задача вычисления нормалей, способная делиться на части
 */
class ComputeNormalsTask(
    private val pixels: Array<Array<Pixel>>,
    private val normals: Array<DoubleArray>,
    private val startRow: Int = 0,
    private val endRow: Int = pixels.size,
) {


    suspend fun compute():Double {
        if (endRow - startRow <= THRESHOLD) {
            var maxNormal:Double = 0.0
            // Обрабатываем строки
            for (i in startRow until endRow) {
                for (j in pixels[i].indices) {
                    val pixel = pixels[i][j]
                    if (pixel.hitCount != 0) {
                        normals[i][j] = log10(pixel.hitCount.toDouble())
                        maxNormal = maxNormal.coerceAtLeast(normals[i][j])
                    }
                }
            }
            return maxNormal
        } else {
            // Разделяем задачу
            val mid = (startRow + endRow) / 2
            val topTask = ComputeNormalsTask(pixels, normals, startRow, mid)
            val bottomTask = ComputeNormalsTask(pixels, normals, mid, endRow)
            return coroutineScope {
                val deferred1 = async { topTask.compute() }
                val deferred2 = async { bottomTask.compute() }
                return@coroutineScope deferred1.await().coerceAtLeast(deferred2.await())
            }
        }
    }

    companion object {
        private const val THRESHOLD = 50 // Минимальное количество строк для разделения задачи
    }
}