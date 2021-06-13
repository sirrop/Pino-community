package com.branc.pino.notification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NotificationCenter implements Publisher {
    private final LinkedList<Notification> history = new LinkedList<>();

    public void notify(Notification notification) {
        System.out.println(notification.getType() + ":" + notification.getTitle());
        System.out.println(notification.getBody());
        System.out.println(notification.getExtendedBody());
        history.add(notification);
    }

    public List<Notification> getHistory() {
        return new ArrayList<>(history);
    }
}
