package backend.academy.settings;

import backend.academy.console.Console;
import backend.academy.image.drawer.ImageSettings;
import backend.academy.util.PropertyUtil;
import java.io.IOException;

public class ImageSettingsAsker extends Asker<ImageSettings> {
    public ImageSettingsAsker(Console console) {
        super(console);
    }

    @Override
    public ImageSettings ask() throws IOException {
        var size = askSize();
        var threadMode = askThreadMode("drawer");
        return new ImageSettings(size[0], size[1], threadMode);
    }

    public int[] askSize() throws IOException {
        console.flush();
        while (true) {
            console.println(PropertyUtil.getMessage("ask.size"));
            var str = console.readLine();
            var size = inputParser.parseIntCouple(str.split(" "));
            if (size != null && size[0] > 0 && size[1] > 0) {
                return size;
            } else {
                console.flush();
                console.println(PropertyUtil.getInvalidArgMessage("size"));
            }
        }

    }

}
