module pino.application.api {
    exports jp.gr.java_conf.alpius.pino.application;
    exports jp.gr.java_conf.alpius.pino.notification;
    exports jp.gr.java_conf.alpius.pino.project;
    exports jp.gr.java_conf.alpius.pino.service;
    exports jp.gr.java_conf.alpius.pino.window;

    exports jp.gr.java_conf.alpius.pino.internal to pino.application.impl, pino.graphics, pino.ui;
    exports jp.gr.java_conf.alpius.pino.tool;
    exports jp.gr.java_conf.alpius.pino.input;
    exports jp.gr.java_conf.alpius.pino.history;

    requires pino.graphics;
    requires org.jetbrains.annotations;
}