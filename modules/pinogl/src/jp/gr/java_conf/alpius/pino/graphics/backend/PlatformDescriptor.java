package jp.gr.java_conf.alpius.pino.graphics.backend;

import jp.gr.java_conf.alpius.pino.graphics.shader.ShaderType;

import java.util.Collection;
import java.util.function.Supplier;

public interface PlatformDescriptor {
    String getName();
    String getVersion();
    String getVendor();
    Collection<ShaderType> getSupportedShaderTypes();
    String toString();
    Supplier<? extends Platform> getProvider();
}
