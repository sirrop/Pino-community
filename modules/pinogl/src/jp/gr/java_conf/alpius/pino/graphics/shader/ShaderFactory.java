package jp.gr.java_conf.alpius.pino.graphics.shader;

import jp.gr.java_conf.alpius.pino.graphics.ResourceFactory;

import java.io.InputStream;

public interface ShaderFactory extends ResourceFactory {
    Shader createShader(InputStream in);
}
