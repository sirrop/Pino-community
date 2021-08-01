package jp.gr.java_conf.alpius.pino.annotations;

import java.lang.annotation.*;

/**
 * 表示の精度を指定します。
 * <p>
 *     Precisionにnが指定されると、小数点第n位まで表示されます。
 *     ただし、0のときは整数で表示されます。
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Precision {
    int value();
}
