package backend.academy

import backend.academy.console.Console
import backend.academy.flame.FlameSettings
import backend.academy.flame.MultiThreadFlame
import backend.academy.flame.SingleThreadFlame
import backend.academy.image.ImageSaver
import backend.academy.image.drawer.ImageSettings
import backend.academy.backend.academy.image.drawer.MultiThreadDrawer
import backend.academy.image.drawer.SingleThreadDrawer
import backend.academy.pixel.Pixels
import backend.academy.settings.FlameSettingsAsker
import backend.academy.settings.ImageSettingsAsker
import backend.academy.util.PropertyUtil
import java.awt.image.BufferedImage
import java.io.*
import java.nio.charset.StandardCharsets

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            Console(
                BufferedReader(InputStreamReader(System.`in`, StandardCharsets.UTF_8)),
                BufferedWriter(OutputStreamWriter(System.out, StandardCharsets.UTF_8))
            ).use { console ->
                val imageSettingsAsker = ImageSettingsAsker(console)
                val imageSettings = imageSettingsAsker.ask()

                val flameSettingsAsker = FlameSettingsAsker(console)
                val flameSettings = flameSettingsAsker.ask()

                val image = getImage(imageSettings, flameSettings)

                val imageSaver = ImageSaver()
                imageSaver.saveImage(image)
            }
        } catch (e: IOException) {
            System.err.println(PropertyUtil.getIOExceptionMessage(e.message))
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }

    private fun getImage(imageSettings: ImageSettings, flameSettings: FlameSettings): BufferedImage {
        val pixels = Pixels(
            imageSettings.width(),
            imageSettings.height(),
            flameSettings.background()
        )

        val flame = when (flameSettings.threadMode()) {
            ThreadMode.SINGLE_THREAD -> SingleThreadFlame(
                flameSettings.listOfFunctions(),
                flameSettings.variations(),
                pixels
            )
            ThreadMode.MULTI_THREAD -> MultiThreadFlame(
                flameSettings.listOfFunctions(),
                flameSettings.variations(),
                pixels
            )
        }

        flame.render(
            flameSettings.samples(),
            flameSettings.iterationCount(),
            flameSettings.symmetry()
        )

        val drawer = when (imageSettings.threadMode()) {
            ThreadMode.SINGLE_THREAD -> SingleThreadDrawer()
            ThreadMode.MULTI_THREAD -> MultiThreadDrawer()
        }

        return drawer.draw(imageSettings, pixels)
    }
}