package jp.gr.java_conf.alpius.pino.graphics.backend;

public abstract class AbstractPlatformDescriptor implements PlatformDescriptor {
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    private final String name;
    private final String version;
    private final String vendor;

    public AbstractPlatformDescriptor(String name, String version, String vendor) {
        this.name = name;
        this.version = version;
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return String.format("%s%s[vendor=%s]", getName(), getVersion(), getVendor());
    }
}
