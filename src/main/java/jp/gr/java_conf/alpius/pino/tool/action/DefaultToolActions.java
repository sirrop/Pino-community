package jp.gr.java_conf.alpius.pino.tool.action;

import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;
import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;

import java.util.List;

public final class DefaultToolActions {
    private DefaultToolActions() {
    }

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
