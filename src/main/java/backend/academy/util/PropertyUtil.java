package backend.academy.util;

import java.util.ResourceBundle;
import lombok.experimental.UtilityClass;

/**
 * Класс для получения сообщений из messages.properties
 */
@UtilityClass
public class PropertyUtil {
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

    public static String getMessage(String key) {
        return MESSAGES.getString(key);
    }

    public static String getInvalidArgMessage(String argName) {
        return String.format(getMessage("invalid.arg"), getMessage(argName));
    }

    public static String getIOExceptionMessage(String message) {
        return String.format(getMessage("on.io.exception"), message);
    }

}
