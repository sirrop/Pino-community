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

import jp.gr.java_conf.alpius.pino.util.StringConverter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public final class PropertyUtils {
    private PropertyUtils() {
        throw new AssertionError();
    }

    public static <T> void set(PropertyDescriptor desc, Object bean, T value) {
        try {
            desc.getWriteMethod().invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Can't access to setter for %s", desc.getName()), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(PropertyDescriptor desc, Object bean) {
        try {
            return (T) desc.getReadMethod().invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Can't access to getter for %s", desc.getName()));
        }
    }

    public static <T> void putValue(PropertyDescriptor desc, Object key, T value) {
        desc.setValue(key.toString(), value);
    }

    public static <T> T getValueElse(PropertyDescriptor desc, Object key, StringConverter<T> converter, T defaultValue) {
        Object value = desc.getValue(key.toString());
        if (value == null) {
            return defaultValue;
        } else {
            return converter.fromString(value.toString());
        }
    }

    public static int getIntElse(PropertyDescriptor desc, Object key, int defaultValue) {
        Object value = desc.getValue(key.toString());
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    public static double getDoubleElse(PropertyDescriptor desc, Object key, double defaultValue) {
        Object value = desc.getValue(key.toString());
        if (value == null) {
            return defaultValue;
        } else {
            return Double.parseDouble(value.toString());
        }
    }

    public static String getStringElse(PropertyDescriptor desc, Object key, String defaultValue) {
        Object value = desc.getValue(key.toString());
        if (value == null) {
            return defaultValue;
        } else {
            return value.toString();
        }
    }
}
