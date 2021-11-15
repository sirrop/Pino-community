package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.backend.AbstractPlatformDescriptor;
import jp.gr.java_conf.alpius.pino.graphics.shader.ShaderType;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class Java2DDescriptor extends AbstractPlatformDescriptor {
    public static final String BACKEND_NAME = "Java2D";
    public static final String VERSION = "0.1.0";
    public static final String VENDOR = "shiro";

    // No shaders are supported.
    public static final Collection<ShaderType> SUPPORTED_SHADER_TYPES = Collections.emptyList();

    private final Supplier<Java2DPlatform> provider = new Supplier<>() {
        private Java2DPlatform platform;

        @Override
        public Java2DPlatform get() {
            if (platform == null) {
                platform = new Java2DPlatform(Java2DDescriptor.this);
            }
            return platform;
        }
    };

    public Java2DDescriptor() {
        super(BACKEND_NAME, VERSION, VENDOR);
    }

    @Override
    public Collection<ShaderType> getSupportedShaderTypes() {
        return SUPPORTED_SHADER_TYPES;
    }

    @Override
    public Supplier<Java2DPlatform> getProvider() {
        return provider;
    }
}
