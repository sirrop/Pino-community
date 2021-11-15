package jp.gr.java_conf.alpius.pino.graphics.transform;

import java.util.Arrays;

public class Affine2D extends Transform {
    private final double[] matrix = new double[9];

    public static Affine2D makeFromFlatMatrix(double[] flatMatrix) {
        Affine2D affine = new Affine2D();
        System.arraycopy(flatMatrix, 0, affine.matrix, 0, 9);
        return affine;
    }

    public static Affine2D makeFromFlatMatrix(float[] flatMatrix) {
        Affine2D affine = new Affine2D();
        for (int i = 0; i < 9; i++) {
            affine.matrix[i] = flatMatrix[i];
        }
        return affine;
    }

    public double[] flatten() {
        return Arrays.copyOf(matrix, 9);
    }
}
