package backend.academy.settings;

import backend.academy.coefficent.Generator;
import backend.academy.console.Console;
import backend.academy.flame.FlameSettings;
import backend.academy.function.Function;
import backend.academy.function.ListOfFunctions;
import backend.academy.pixel.Color;
import backend.academy.pixel.ColorNamed;
import backend.academy.transform.TransformerNamed;
import backend.academy.util.PropertyUtil;
import backend.academy.variation.Variation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlameSettingsAsker extends Asker<FlameSettings> {
    public FlameSettingsAsker(Console console) {
        super(console);
    }

    @Override
    public FlameSettings ask() throws IOException {
        var backgroundColor = askBackgroundColor();
        var iterationCount = askIterationCount();
        var sampleCount = askSamplesCount();
        var symmetryCount = askSymmetryCount();
        var listOfVariations = askListOfVariations();
        var listOfFunctions = askListOfFunctions();
        var threadMode = askThreadMode("flame");
        return new FlameSettings(iterationCount, backgroundColor, listOfFunctions,
            listOfVariations, symmetryCount, sampleCount, threadMode);
    }

    List<Variation> askListOfVariations() throws IOException {
        List<Variation> variations = new ArrayList<>();
        TransformerNamed[] values = TransformerNamed.values();

        while (variations.isEmpty()) {
            console.println(PropertyUtil.getMessage("ask.transformers"));
            printlnAllEnumValues(values);
            var str = console.readLine();
            try {
                var numbers = Arrays.stream(str.trim().split(" ")).map(Integer::parseInt).toList();
                for (var number : numbers) {
                    variations.add(new Variation(1.0 / numbers.size(), values[number - 1].transformer()));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                variations.clear();
                console.flush();
                console.println(PropertyUtil.getInvalidArgMessage("transformers"));
            }
        }
        return variations;
    }

    ListOfFunctions askListOfFunctions() throws IOException {
        var count = askPositiveInt("ask.count.of.functions", "count.of.functions");
        ListOfFunctions listOfFunctions = new ListOfFunctions();
        double prob = 1.0 / count;
        var generator = new Generator();
        for (int i = 0; i < count; i++) {
            var color = askColorOfFunction();
            listOfFunctions.addFunction(new Function(generator.generate(), color), prob);
        }
        return listOfFunctions;
    }

    int askSymmetryCount() throws IOException {
        return askPositiveInt("ask.symmetry.count", "symmetry.count");
    }

    int askIterationCount() throws IOException {
        return askPositiveInt("ask.iteration.count", "iteration.count");
    }

    int askSamplesCount() throws IOException {
        return askPositiveInt("ask.samples.count", "samples.count");
    }

    Color askBackgroundColor() throws IOException {
        return askColor("ask.background.color", "background.color");
    }

    Color askColorOfFunction() throws IOException {
        return askColor("ask.function.color", "function.color");
    }

    Color askColor(String messageKey, String argKey) throws IOException {
        return askEnumValues(ColorNamed.values(), messageKey, argKey).color();
    }

    private int askPositiveInt(String messageKey, String argKey) throws IOException {
        console.flush();
        int number = -1;
        while (number <= 0) {
            console.println(PropertyUtil.getMessage(messageKey));
            var str = console.readLine();
            var parsedNumber = inputParser.parseNumber(str);
            if (parsedNumber > 0) {
                number = parsedNumber;
            } else {
                console.flush();
                console.println(PropertyUtil.getInvalidArgMessage(argKey));
            }
        }
        return number;
    }
}
