package jp.gr.java_conf.alpius.pino.graphics;

import java.nio.Buffer;

public interface PixelModel {
    int getWidth();
    int getHeight();
    double[] getPixel(int x, int y, double[] pixel, Buffer data);
    float[] getPixel(int x, int y, float[] pixel, Buffer data);
    int[] getPixel(int x, int y, int[] pixel, Buffer data);
    double[] getPixels(int x, int y, int w, int h, double[] pixels, Buffer data);
    float[] getPixels(int x, int y, int w, int h, float[] pixels, Buffer data);
    int[] getPixels(int x, int y, int w, int h, int[] pixels, Buffer data);

    void setPixel(int x, int y, double[] pixel, Buffer data);
    void setPixel(int x, int y, float[] pixel, Buffer data);
    void setPixel(int x, int y, int[] pixel, Buffer data);
    void setPixels(int x, int y, int w, int h, double[] pixels, Buffer data);
    void setPixels(int x, int y, int w, int h, float[] pixels, Buffer data);
    void setPixels(int x, int y, int w, int h, int[] pixels, Buffer data);
}
