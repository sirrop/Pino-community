package com.branc.pino.ui.actionSystem;

public class ActionNotFoundException extends Exception {
    private final String actionId;

    public ActionNotFoundException(String actionId) {
        super(String.format("An action[id: %s] was not found.", actionId));
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }
}
