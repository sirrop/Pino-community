package jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts

import jp.gr.java_conf.alpius.pino.application.ApplicationManager
import jp.gr.java_conf.alpius.pino.notification.Notification
import jp.gr.java_conf.alpius.pino.notification.NotificationCenter
import jp.gr.java_conf.alpius.pino.notification.NotificationType
import groovy.transform.Field

def notification = new Notification(
        "未実装のアクション",
        "アクション[id=${args[0]}]は未実装です",
        "",
        NotificationType.INFO,
        List.of()
)

ApplicationManager.app.getService(NotificationCenter.class).notify(notification)