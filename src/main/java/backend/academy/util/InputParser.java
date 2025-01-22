package backend.academy.util;

import backend.academy.flame.MultiThreadFlame;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class InputParser {
    /**
     * Парсит число из строки, кроме числа -1
     *
     * @return число если успешно, -1 если не успешно
     */
    public int parseNumber(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Преобразует массив строк длины 2 в массив чисел длины 2
     *
     * @return массив чисел длины 2 или null при неудаче
     */
    @SuppressFBWarnings(value = "CLI_CONSTANT_LIST_INDEX") // Примените здесь, если это необходимо
    public int[] parseIntCouple(String[] parts) {
        if (parts.length != 2) {
            return null;
        }
        try {
            var first = Integer.parseInt(parts[0]);
            var second = Integer.parseInt(parts[1]);
            return new int[] {first, second};
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
