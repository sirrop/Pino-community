package com.branc.pino.notification;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getExtendedBody() {
        return extendedBody;
    }

    public NotificationType getType() {
        return type;
    }

    public List<NotificationAction> getActions() {
        return new ArrayList<>(actions);
    }

    private final String title;
    private final String body;
    private final String extendedBody;
    private final NotificationType type;
    private final List<NotificationAction> actions;

    public Notification(
            String title,
            String body,
            String extendedBody,
            NotificationType type,
            List<NotificationAction> actions
    ) {
        this.title = title;
        this.body = body;
        this.extendedBody = extendedBody;
        this.type = type;
        this.actions = actions;
    }
}
