package backend.academy.settings;

import backend.academy.ThreadMode;
import backend.academy.console.Console;
import backend.academy.util.InputParser;
import backend.academy.util.PropertyUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Asker<T> {
    protected final InputParser inputParser = new InputParser();

    protected final Console console;

    public abstract T ask() throws IOException;

    protected ThreadMode askThreadMode(String postfix) throws IOException {
        return askEnumValues(ThreadMode.values(), "ask.thread.mode." + postfix, "thread.mode." + postfix);
    }

    protected <E> E askEnumValues(E[] values, String messageKey, String argKey) throws IOException {
        console.flush();
        E value = null;
        while (value == null) {
            console.println(PropertyUtil.getMessage(messageKey));
            printlnAllEnumValues(values);
            var str = console.readLine();
            var number = inputParser.parseNumber(str);
            if (0 < number && number <= values.length) {
                value = values[number - 1];
            } else {
                console.flush();
                console.println(PropertyUtil.getInvalidArgMessage(argKey));
            }
        }
        return value;
    }

    protected <E> void printlnAllEnumValues(E[] values) throws IOException {
        for (int i = 0; i < values.length; i++) {
            E value = values[i];
            console.println((i + 1) + ". " + PropertyUtil.getMessage(value.toString().toLowerCase()));
        }
    }

}
