package backend.academy.settings;

import backend.academy.flame.FlameSettings;
import backend.academy.image.drawer.ImageSettings;

public record Settings(ImageSettings imageSettings,
                       FlameSettings flameSettings) {
}
