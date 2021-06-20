package jp.gr.java_conf.alpius.pino.core.util;

import org.jetbrains.annotations.Nullable;

public final class Strings {
    private Strings() {}

    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
