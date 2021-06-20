package jp.gr.java_conf.alpius.pino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

/**
 * クラスとリソースバンドルをバインドします。
 * <p>
 * リソースバンドルは、{@link java.util.ResourceBundle#getBundle(String, Locale, ClassLoader)}を使用して取得されることに注意してください。
 * 使用されるClassLoaderは、対象クラスのクラスローダーです。
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ResourceBundle {
    String base();
}
