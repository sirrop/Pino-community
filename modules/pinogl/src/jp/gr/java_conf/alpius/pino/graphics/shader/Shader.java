package jp.gr.java_conf.alpius.pino.graphics.shader;

import jp.gr.java_conf.alpius.pino.graphics.GraphicsResource;

public interface Shader extends GraphicsResource {
    void enable();
    void disable();
}
