package backend.academy.backend.academy.image.drawer

import backend.academy.image.drawer.Drawer
import backend.academy.image.drawer.ImageSettings
import backend.academy.pixel.Pixels
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask

class MultiThreadDrawer : Drawer {
    override fun draw(imageSettings: ImageSettings, pixels: Pixels): BufferedImage {
        // Определяем размеры изображения
        val width = imageSettings.width
        val height = imageSettings.height

        // Создаем пустое изображение
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val task = DrawTask(image, pixels,width,height)
        runBlocking {
            task.compute()
        }

        return image
    }

    /**
     * Рекурсивная задача для отрисовки
     */
    private class DrawTask(
        private val image: BufferedImage,
        private val pixels: Pixels,
        private val width: Int,
        private val height: Int,
        private val startRow: Int = 0,
        private val endRow: Int = pixels.getPixels().size // Конечная строка (не включительно)
    ) {
        suspend fun compute() {
            // Если количество строк меньше порога, выполняем задачу напрямую
            if (endRow - startRow <= THRESHOLD) {
                drawPixels()
            }else {
                // Делим задачу на две подзадачи
                val mid = (startRow + endRow) / 2
                val topTask = DrawTask( image, pixels, width, height,startRow, mid)
                val bottomTask = DrawTask( image, pixels, width, height,mid, endRow)
                coroutineScope {
                    val job1 = launch {
                        topTask.compute()
                    }
                    val job2 = launch {
                        bottomTask.compute()
                    }
                    job1.join()
                    job2.join()
                }
            }
        }

        // Метод для непосредственной отрисовки пикселей
        fun drawPixels() {
            val pixelArray = pixels.getPixels()

            for (row in startRow..<endRow) {
                for (col in pixelArray[row].indices) {
                    val pixel = pixelArray[row][col]

                    val color = Color(pixel.color.red, pixel.color.green, pixel.color.blue)

                    // Преобразуем координаты пикселя в пространство изображения
                    val normalizedX = col.toDouble() / pixelArray[row].size // Нормализация в диапазон [0, 1]
                    val normalizedY = row.toDouble() / pixelArray.size // Нормализация в диапазон [0, 1]

                    val x = Math.round(normalizedX * (width - 1)).toInt() // Масштабируем в [0, width - 1]
                    val y = Math.round(normalizedY * (height - 1)).toInt() // Масштабируем в [0, height - 1]

                    // Устанавливаем цвет пикселя в изображении
                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        image.setRGB(x, y, color.rgb)
                    }
                }
            }
        }

        companion object {
            private const val THRESHOLD = 50 // Максимальное количество строк для одной задачи
        }
    }
}