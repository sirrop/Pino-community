package jp.gr.java_conf.alpius.pino.graphics.utils;

public final class Checks {
    private Checks() {
    }

    public static void require(boolean condition, String mes) {
        if (!condition) {
            throw new IllegalArgumentException(mes);
        }
    }

    public static void check(boolean condition, String mes) {
        if (!condition) {
            throw new IllegalStateException(mes);
        }
    }
}
