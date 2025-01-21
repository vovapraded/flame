package backend.academy.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

public class ImageSaver {
    /**
     * Сохранить изображение в место, где была запущена программа
     */
    public void saveImage(BufferedImage image) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        Path path = Path.of("result_" + timestamp + ".png");
        ImageIO.write(image, "png", path.toFile());
    }
}
