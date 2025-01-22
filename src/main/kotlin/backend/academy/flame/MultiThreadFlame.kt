package backend.academy.flame

import ApplyGammaTask
import backend.academy.exception.UnexpectedException
import backend.academy.backend.academy.flame.task.ComputeNormalsTask
import backend.academy.function.Function
import backend.academy.function.ListOfFunctions
import backend.academy.pixel.Pixel
import backend.academy.pixel.Pixels
import backend.academy.variation.Variation
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference

class MultiThreadFlame(listOfFunctions: ListOfFunctions?, variations: List<Variation?>?, pixels: Pixels?) :
    Flame(listOfFunctions, variations, pixels) {

    @Throws(UnexpectedException::class)
    override fun render(sampleCount: Int, iterationPerSample: Int, symmetry: Int) {

        runBlocking {
            val jobList = ArrayList<Job?>()
            repeat (sampleCount) {
                val job = launch {
                    renderSample(iterationPerSample, symmetry)
                }
                jobList.add(job)
            }
            jobList.forEach { it?.join() }
        }



        gammaCorrection()
    }

    override fun updatePixel(indexes: IntArray, function: Function) {
        val pixels1: Array<Array<Pixel>> = pixels.getPixels();
        val pixelRef = AtomicReference(pixels1[indexes[0]][indexes[1]])
        pixelRef.updateAndGet { currPixel: Pixel ->
            val newColor =
                if (currPixel.hitCount == 0) function.startColor else currPixel.color.mixWith(function.startColor)
            Pixel(newColor, currPixel.hitCount + 1)
        }
        pixels1[indexes[0]][indexes[1]]  = pixelRef.get()
    }

    override fun computeNormals(normals: Array<DoubleArray>): Double {
        return runBlocking {
            // Создаем задачу и выполняем её
            val task = ComputeNormalsTask(pixels.getPixels(), normals)
            return@runBlocking  task.compute()  // Здесь мы вызываем compute() для выполнения задачи
        }
    }

    override fun applyGammaCorrection(normals: Array<DoubleArray>, max: Double) {
        runBlocking {
            // Создаем задачу и выполняем её
            val task = ApplyGammaTask(pixels.getPixels(), normals, Flame.GAMMA,max)
            task.compute()  // Здесь мы вызываем compute() для выполнения задачи
        }
    }
}
