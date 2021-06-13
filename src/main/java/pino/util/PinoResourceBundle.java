package pino.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PinoResourceBundle extends ResourceBundle {
    private final ResourceBundle thisBundle;
    private final ResourceBundle parent;

    private PinoResourceBundle(ResourceBundle thisBundle, ResourceBundle parent) {
        this.thisBundle = thisBundle;
        this.parent = parent;
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        try {
            return thisBundle.getObject(key);
        } catch (MissingResourceException ignored) {
            if (parent == null) return null;
            return parent.getObject(key);
        }
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        return new Enumeration<>() {
            final Enumeration<String> thisEnumeration = thisBundle.getKeys();
            final Enumeration<String> parentEnumeration = parent == null ? null : parent.getKeys();

            @Override
            public boolean hasMoreElements() {
                return !thisEnumeration.hasMoreElements() && parentEnumeration != null && parentEnumeration.hasMoreElements();
            }

            @Override
            public String nextElement() {
                if (thisEnumeration.hasMoreElements() || parentEnumeration == null) {
                    return thisEnumeration.nextElement();
                } else {
                    return parentEnumeration.nextElement();
                }
            }
        };
    }

    @Nullable
    public static PinoResourceBundle of(ResourceBundle thisBundle, ResourceBundle parent) {
        if (thisBundle == null) return null;
        return new PinoResourceBundle(thisBundle, parent);
    }
}
