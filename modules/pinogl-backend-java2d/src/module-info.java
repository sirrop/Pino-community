import jp.gr.java_conf.alpius.pino.graphics.backend.PlatformDescriptor;
import jp.gr.java_conf.alpius.pino.graphics.backend.java2d.Java2DDescriptor;

module pinogl.backend.java2d {
    exports jp.gr.java_conf.alpius.pino.graphics.backend.java2d;
    requires java.desktop;
    requires pinogl;
    provides PlatformDescriptor with Java2DDescriptor;
}