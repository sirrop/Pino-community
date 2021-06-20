package jp.gr.java_conf.alpius.pino.ui.actionSystem;

import java.util.EventListener;

public abstract class Action implements EventListener {
    private final String actionId;
    private final String description;

    protected Action(String actionId, String description) {
        this.actionId = actionId;
        this.description = description;
    }

    public final String getActionID() {
        return actionId;
    }

    public final String getDescription() {
        return description;
    }

    public abstract void performed(ActionEvent e);
}
