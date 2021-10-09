/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.beans;

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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {
    String id() default "";
}