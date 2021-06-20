package jp.gr.java_conf.alpius.pino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * このアノテーションがつけられたフィールドは通常表示されず、拡張編集になると表示されます。
 * <p>
 *     このアノテーションがつけられたフィールドは、{@link Bind}をつける必要はありません。
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForEx {
}
