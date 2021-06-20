package jp.gr.java_conf.alpius.pino.annotations;

import jp.gr.java_conf.alpius.pino.ui.attr.ViewType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface View {
    ViewType value();
}
