package pino.beans;

import com.branc.pino.core.annotaion.Internal;
import com.branc.pino.core.util.StringConverter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

@Internal
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
            return converter.convert(value.toString());
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
