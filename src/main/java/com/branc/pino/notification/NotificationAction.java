package com.branc.pino.notification;

import com.branc.pino.ui.actionSystem.Action;

public abstract class NotificationAction extends Action {
    private final String text;

    public NotificationAction(String text, String actionId, String description) {
        super(actionId, description);
        this.text = text;
    }

    public NotificationAction(String text, String actionId) {
        this(text, actionId, "");
    }

    public final String getText() {
        return text;
    }
}
