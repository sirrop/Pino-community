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

package jp.gr.java_conf.alpius.pino.notification;

import java.awt.*;
import java.util.Objects;

public class NotificationBuilder {
    private NotificationType type;
    private String title;
    private String body;
    private Image icon;

    public NotificationBuilder setType(NotificationType type) {
        this.type = type;
        return this;
    }

    public NotificationBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public NotificationBuilder setIcon(Image icon) {
        this.icon = icon;
        return this;
    }

    public Notification build() {
        var title = Objects.requireNonNull(this.title);
        var body = Objects.requireNonNull(this.body);
        var icon = this.icon;
        var type = Objects.requireNonNull(this.type);
        return new Notification(title, body, icon, type);
    }
}
