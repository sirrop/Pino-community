package com.branc.pino.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UIとバインドするプロパティを示します。
 * <p>
 * このアノテーションがつけられたフィールドは、getter/setterが用意されている必要があります。
 * メソッド名はキャメルケースで記述し、以下のルールに従ってください。XXXはフィールド名が入ります。
 * </p>
 * <table>
 *     <th> <td>Getter</td> <td>Setter</td> </th>
 *     <tr> <td>boolean and Boolean</td> <td>isXXX</td> <td>setXXX</td> </tr>
 *     <tr> <td>others</td> <td>getXXX</td> <td>setXXX</td> </tr>
 * </table>
 * <p>
 *     上記のルールに従わないプロパティのgetterおよびsetterを使用する場合は、{@link Accessor}を使用して明示してください。
 * </p>
 * <p>
 *     クラスに{@link ResourceBundle}が付されている場合、そのアノテーションで結び付けられたリソースバンドルで表示する名前を検索します。
 *     検索に使用されるキーはデフォルトでは[フィールド名].nameですが、idを指定することでそのidにキーを変更することができます。
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {
    String id() default "";
}