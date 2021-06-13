package pino.util;

import com.branc.pino.application.ApplicationError;
import com.branc.pino.core.annotaion.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Internal
public final class Utils {
    private Utils() {
    }

    public static void forceInit(Class<?> initClass) {
        try {
            Class.forName(initClass.getName(), true, initClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new ApplicationError(e);
        }
    }

    private static final Map<Class<?>, ResourceBundle> cache = new HashMap<>();

    @Nullable
    public static ResourceBundle findBundle(Class<?> klass) {
        @Nullable ResourceBundle res = cache.get(klass);
        if (res == null) {
            res = validateBundle(klass);
            if (res != null) cache.put(klass, res);
        }
        return res;
    }

    @Nullable
    private static ResourceBundle validateBundle(Class<?> beanClass) {
        @Nullable ResourceBundle res = PinoResourceBundle.of(getBundle(beanClass), null);
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

    @Nullable
    private static ResourceBundle getBundle(@NotNull Class<?> beanClass) {
        var ann = beanClass.getAnnotation(com.branc.pino.annotations.ResourceBundle.class);
        if (ann == null) return null;
        String base = ann.base();
        return ResourceBundle.getBundle(base, Locale.getDefault(), beanClass.getClassLoader());
    }
}
