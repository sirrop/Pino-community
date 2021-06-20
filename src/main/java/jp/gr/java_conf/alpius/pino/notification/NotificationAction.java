package jp.gr.java_conf.alpius.pino.notification;

import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;

public abstract class NotificationAction extends Action {
    public static final NotificationAction[] EMPTY = new NotificationAction[0];

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
