package com.branc.pino.tool.action;

import com.branc.pino.application.ApplicationManager;
import com.branc.pino.ui.actionSystem.Action;
import com.branc.pino.ui.actionSystem.ActionEvent;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.List;

public final class DefaultToolActions {
    private DefaultToolActions() {}

    public static List<Class<? extends Action>> get() {
        return List.of(
                ActivateDrawTool.class,
                ActivateHandTool.class
        );
    }


    public static class ActivateDrawTool extends Action {
        public static final String ID = "pino:tools:activate-draw-tool";
        public static final String DESCRIPTION = "描画ツールを有効化します";

        public ActivateDrawTool() {
            super(ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            ApplicationManager.getApp().getEventDistributor().activateDrawTool();
        }
    }

    public static class ActivateHandTool extends Action {
        public static final String ID = "pino:tools:activate-hand-tool";
        public static final String DESCRIPTION = "移動ツールを有効化します";

        public ActivateHandTool() {
            super(ID, DESCRIPTION);
        }

        public void performed(ActionEvent e) {
            ApplicationManager.getApp().getEventDistributor().activateHandTool();
        }
    }
}
