package com.branc.pino.ui.actionSystem.scripts

import com.branc.pino.application.ApplicationManager
import com.branc.pino.notification.Notification
import com.branc.pino.notification.NotificationCenter
import com.branc.pino.notification.NotificationType
import groovy.transform.Field

@Field
def notification = new Notification(
        "未実装",
        "このアクションは未実装です",
        "",
        NotificationType.INFO,
        List.of()
)

ApplicationManager.app.getService(NotificationCenter.class).notify(notification)