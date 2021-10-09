/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.application.impl;

import jp.gr.java_conf.alpius.pino.notification.Notification;
import jp.gr.java_conf.alpius.pino.notification.Publisher;
import org.controlsfx.control.Notifications;

import java.util.LinkedList;
import java.util.List;

final class NotificationCenter implements Publisher {
    public static final int DEFAULT_CAPACITY = 100;
    private int capacity = DEFAULT_CAPACITY;
    private final LinkedList<Notification> history = new LinkedList<>();

    @Override
    public void publish(Notification notification) {
        var notifications = Notifications.create()
                .title(notification.title())
                .text(notification.body());
        switch (notification.type()) {
            case INFO -> notifications.showInformation();
            case WARN -> notifications.showWarning();
            case ERROR -> notifications.showError();
            case CONFIRM -> notifications.showConfirm();
            case UPDATE -> notifications.show();
        }
        if (history.size() >= capacity) {
            history.removeFirst();
        }
        history.add(notification);
    }

    @Override
    public List<Notification> getHistory() {
        return List.copyOf(history);
    }

    @Override
    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0");
        }
        this.capacity = capacity;
        trimHistory();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    private void trimHistory() {
        while (history.size() > capacity) {
            history.removeFirst();
        }
    }
}
