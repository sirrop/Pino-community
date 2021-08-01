package jp.gr.java_conf.alpius.pino.service;

import java.lang.annotation.*;

public final class Service {
    private Service() {}

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Application {

    }
}
