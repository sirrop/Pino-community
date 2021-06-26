package jp.gr.java_conf.alpius.pino.filter;

import java.awt.image.Kernel;
import java.util.Arrays;

public final class Kernels {
    private Kernels() {
        throw new AssertionError();
    }

    public static Kernel createMeanKernel(int width, int height) {
        int size = width * height;
        float[] kernel = new float[size];
        float value = 1f / size;
        Arrays.fill(kernel, value);
        return new Kernel(width, height, kernel);
    }

    public static Kernel createOnes(int width, int height) {
        int size = width * height;
        float[] kernel = new float[size];
        Arrays.fill(kernel, 1f);
        return new Kernel(width, height, kernel);
    }
}
