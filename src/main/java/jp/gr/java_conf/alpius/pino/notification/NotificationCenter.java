package jp.gr.java_conf.alpius.pino.notification;

import org.controlsfx.control.Notifications;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NotificationCenter implements Publisher {
    private final LinkedList<Notification> history = new LinkedList<>();

    public void notify(Notification notification) {
        Notifications notifications = Notifications.create()
                .title(notification.getTitle())
                .text(notification.getBody());
        switch (notification.getType()) {
            case INFO:
                notifications.showInformation();
                break;
            case WARN:
                notifications.showWarning();
                break;
            case ERROR:
                notifications.showError();
                break;
            case CONFIRM:
                notifications.showConfirm();
                break;
            case UPDATE:
                notifications.show();
                break;
        }
        history.add(notification);
    }

    public List<Notification> getHistory() {
        return new ArrayList<>(history);
    }
}
