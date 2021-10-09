module pino.core {
    exports jp.gr.java_conf.alpius.pino.annotation;
    exports jp.gr.java_conf.alpius.pino.beans;
    exports jp.gr.java_conf.alpius.pino.disposable;
    exports jp.gr.java_conf.alpius.pino.memento;
    exports jp.gr.java_conf.alpius.pino.util;

    requires com.google.common;
    requires transitive java.desktop;
    requires org.jetbrains.annotations;
}