package backend.academy.function;

import backend.academy.exception.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListOfFunctions {
    private final List<FunctionWithProbability> functionsWithProbs = new ArrayList<>();
    private final List<Double> probabilityIntervals = new ArrayList<>(); // Интервалы вероятностей для быстрого выбора
    private double totalProbability = 0; // Накопленная сумма вероятностей

    /**
     * Метод для добавления функции с вероятностью
     */
    public void addFunction(Function function, double probability) {
        if (probability <= 0) {
            throw new IllegalArgumentException("on.negative.probability");
        }

        // Добавляем функцию в список
        functionsWithProbs.add(new FunctionWithProbability(function, probability));
        totalProbability += probability;

        // Обновляем интервалы вероятностей
        probabilityIntervals.add(totalProbability);

    }

    /**
     * Метод для выбора случайной функции с учетом вероятностей
     */
    public Function getRandomFunction(Random random) throws IllegalArgumentException {

        double rand = random.nextDouble() * totalProbability;

        for (int i = 0; i < probabilityIntervals.size(); i++) {
            if (rand <= probabilityIntervals.get(i)) {
                return functionsWithProbs.get(i).function();
            }
        }

        return functionsWithProbs.getLast().function();
    }

    private record FunctionWithProbability(Function function, double probability) {
    }
}
