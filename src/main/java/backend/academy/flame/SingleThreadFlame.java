package backend.academy.flame;

import backend.academy.function.Function;
import backend.academy.function.ListOfFunctions;
import backend.academy.pixel.Pixel;
import backend.academy.pixel.Pixels;
import backend.academy.variation.Variation;
import java.util.List;

public class SingleThreadFlame extends Flame implements GammaCorrector {

    public SingleThreadFlame(ListOfFunctions listOfFunctions, List<Variation> variations, Pixels pixels) {
        super(listOfFunctions, variations, pixels);
    }

    @Override
    public void render(int sampleCount, int iterationPerSample, int symmetry) {
        for (int i = 0; i < sampleCount; i++) {
            renderSample(iterationPerSample, symmetry);
        }
        gammaCorrection();
    }

    @Override protected void updatePixel(int[] indexes, Function function) {
        var currPixel = pixels.pixels()[indexes[0]][indexes[1]];
        var newColor =
            currPixel.hitCount() == 0 ? function.startColor() : currPixel.color().mixWith(function.startColor());
        Pixel newPixel = new Pixel(newColor, currPixel.hitCount() + 1);
        pixels.pixels()[indexes[0]][indexes[1]] = newPixel;
    }

    @Override
    protected double computeNormals(double[][] normals) {
        double maxNormal = 0;
        for (int i = 0; i < pixels.pixels().length; i++) {
            var row = pixels.pixels()[i];
            for (int j = 0; j < row.length; j++) {
                var pixel = row[j];
                if (pixel.hitCount() != 0) {
                    normals[i][j] = Math.log10(pixel.hitCount());
                    maxNormal = Math.max(maxNormal, normals[i][j]);
                }
            }
        }
        return maxNormal;
    }

    @Override
    protected void applyGammaCorrection(double[][] normals, double max) {
        for (int i = 0; i < pixels.pixels().length; i++) {
            var row = pixels.pixels()[i];
            for (int j = 0; j < row.length; j++) {
                correct(pixels.pixels(), i, j, GAMMA, max, normals);
            }
        }
    }

}
