package jp.gr.java_conf.alpius.pino.graphics;

public interface ResourceFactory {
    Texture createTexture(Image image);
    boolean isFormatSupported(PixelFormat format);
    boolean isCompatibleTexture(Texture texture);
}
