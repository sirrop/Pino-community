module pino.graphics {
    exports jp.gr.java_conf.alpius.pino.graphics.brush;
    exports jp.gr.java_conf.alpius.pino.graphics.brush.event;
    exports jp.gr.java_conf.alpius.pino.graphics.canvas;
    exports jp.gr.java_conf.alpius.pino.graphics.filter;
    exports jp.gr.java_conf.alpius.pino.graphics;
    exports jp.gr.java_conf.alpius.pino.graphics.layer;
    exports jp.gr.java_conf.alpius.pino.graphics.internal.graphics;

    requires transitive java.desktop;
    requires transitive pino.core;
    requires org.jetbrains.annotations;
}