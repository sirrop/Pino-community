package jp.gr.java_conf.alpius.pino.graphics.backend;

import jp.gr.java_conf.alpius.pino.graphics.*;

import java.util.Collection;
import java.util.Objects;

public abstract class Platform {

    protected Platform(PlatformDescriptor desc) {
        this.desc = Objects.requireNonNull(desc, "desc == null");
    }

    private final PlatformDescriptor desc;

    public final PlatformDescriptor getDescriptor() {
        return desc;
    }

    public abstract ResourceFactory getResourceFactory();
    public abstract Collection<? extends PixelFormat> getAvailableFormats();
}
