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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

final class Utils {
    private Utils() {
    }

    private static final Map<Class<?>, ResourceBundle> cache = new HashMap<>();

    public static ResourceBundle findBundle(Class<?> klass) {
        ResourceBundle res = cache.get(klass);
        if (res == null) {
            res = validateBundle(klass);
            if (res != null) cache.put(klass, res);
        }
        return res;
    }

    private static ResourceBundle validateBundle(Class<?> beanClass) {
        ResourceBundle res = PinoResourceBundle.of(getBundle(beanClass), null);
        beanClass = beanClass.getSuperclass();

        while (beanClass != Object.class && res == null) {
            res = PinoResourceBundle.of(getBundle(beanClass), null);
            beanClass = beanClass.getSuperclass();
        }

        if (res == null || beanClass == Object.class) return res;

        ResourceBundle last = res;

        Class<?> superClass = beanClass.getSuperclass();
        while (superClass != Object.class) {
            ResourceBundle parent = getBundle(superClass);
            if (parent != null) {
                last = PinoResourceBundle.of(last, parent);
            }
            superClass = superClass.getSuperclass();
        }

        return last;
    }

    private static ResourceBundle getBundle(Class<?> beanClass) {
        var ann = beanClass.getAnnotation(jp.gr.java_conf.alpius.pino.annotation.ResourceBundle.class);
        if (ann == null) return null;
        String base = ann.base();
        return ResourceBundle.getBundle(base, Locale.getDefault(), beanClass.getClassLoader());
    }

    public static boolean isIntType(Class<?> type) {
        return type == Integer.TYPE ||
                type == Long.TYPE ||
                type == Integer.class ||
                type == Long.class ||
                type == Byte.TYPE ||
                type == Character.TYPE ||
                type == Short.TYPE ||
                type == Byte.class ||
                type == Character.class ||
                type == Short.class;
    }
}
