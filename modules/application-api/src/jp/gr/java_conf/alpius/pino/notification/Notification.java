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

public record Notification(
        String title,
        String body,
        Image icon,
        NotificationType type
) {
    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public static NotificationBuilder error() {
        return builder().setType(NotificationType.ERROR);
    }

    public static NotificationBuilder info() {
        return builder().setType(NotificationType.INFO);
    }

    public static NotificationBuilder warn() {
        return builder().setType(NotificationType.WARN);
    }

    public static NotificationBuilder update() {
        return builder().setType(NotificationType.UPDATE);
    }

    public static NotificationBuilder confirm() {
        return builder().setType(NotificationType.CONFIRM);
    }
}
