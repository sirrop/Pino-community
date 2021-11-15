package jp.gr.java_conf.alpius.pino.graphics;

public interface Composer extends GraphicsResource {
    void compose(Image src, Image dstIn, Image dstOut);
}
