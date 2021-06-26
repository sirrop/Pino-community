package jp.gr.java_conf.alpius.pino.internal.filter.action;

import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;

import java.util.List;

public final class FilterActions {
    private FilterActions() {
        throw new AssertionError();
    }

    public static List<Class<? extends Action>> getActions() {
        return List.of(MeanBlur.class);
    }
}
