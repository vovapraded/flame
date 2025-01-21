package backend.academy.coefficent;

import java.security.SecureRandom;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

public class Generator {
    private static final double BOUND_FOR_SECONDARY_COEFS = 0.25;
    private final SecureRandom random = new SecureRandom();

    /**
     * Генерирует коэффициенты
     *
     * @return объект коэффициент
     */
    @SuppressWarnings("MagicNumber")
    public Coefficient generate() {
        Coefficient coefficient = null;
        while (coefficient == null) {
            var mainCoefs = generateMainCoefficients();
            var sumOfSquares = Arrays.stream(mainCoefs).map(coef -> Math.pow(coef, 2)).sum();
            if (sumOfSquares < 1 + Math.pow(mainCoefs[0] * mainCoefs[3] - mainCoefs[1] * mainCoefs[2],
                2)) {
                coefficient =
                    new Coefficient(mainCoefs[0], mainCoefs[2], generateMinorCoefficient(), mainCoefs[1], mainCoefs[3],
                        generateMinorCoefficient());
            }
        }

        return coefficient;
    }

    /**
     * Генерирует коэффициенты a, b, d, e
     *
     * @return массив [a, d, b, e]
     */
    private double[] generateMainCoefficients() {
        return ArrayUtils.addAll(generatePairCoefficients(), generatePairCoefficients());
    }

    /**
     * Генерирует пару коэффициентов a и d или b и e
     *
     * @return пару в виде массива
     */
    double[] generatePairCoefficients() {
        double first = random.nextDouble(-1, 1);
        double boundForSecond = Math.sqrt(1 - Math.pow(first, 2));
        double second = random.nextDouble(-boundForSecond, boundForSecond);
        return new double[] {first, second};
    }

    /**
     * Генерирует коэффициент c или f
     *
     * @return коэффициент
     */
    private double generateMinorCoefficient() {
        return random.nextDouble(-BOUND_FOR_SECONDARY_COEFS, BOUND_FOR_SECONDARY_COEFS);
    }
}
