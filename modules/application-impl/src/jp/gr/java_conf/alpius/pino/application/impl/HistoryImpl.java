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

import com.google.common.flogger.FluentLogger;
import jp.gr.java_conf.alpius.pino.history.History;
import jp.gr.java_conf.alpius.pino.history.HistoryElement;
import jp.gr.java_conf.alpius.pino.notification.Notification;
import jp.gr.java_conf.alpius.pino.notification.NotificationType;
import jp.gr.java_conf.alpius.pino.notification.Publisher;

import java.util.LinkedList;
import java.util.Objects;

final class HistoryImpl implements History {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private final LinkedList<HistoryElement> undoStack = new LinkedList<>();
    private final LinkedList<HistoryElement> redoStack = new LinkedList<>();

    private final int capacity;

    public HistoryImpl(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0");
        }
        this.capacity = capacity;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getPosition() {
        return undoStack.size() - 1;
    }

    @Override
    public void add(HistoryElement e) {
        Objects.requireNonNull(e, "element is null");
        redoStack.clear();
        undoStack.add(e);
        while (undoStack.size() > capacity) {
            undoStack.removeFirst();
        }
    }

    @Override
    public boolean hasPrevious() {
        return undoStack.size() > 0;
    }

    @Override
    public boolean hasNext() {
        return redoStack.size() > 0;
    }

    private HistoryElement current() {
        return undoStack.getLast();
    }

    @Override
    public void undo() {
        if (hasPrevious()) {
            current().undo();
            redoStack.add(undoStack.removeLast());
        } else {
            Notification notification = new Notification(
                    "操作を戻すことが出来ません",
                    "これ以上操作を戻すことはできません",
                    null,
                    NotificationType.WARN
            );
            Pino.getApp().getService(Publisher.class).publish(notification);
        }
    }

    @Override
    public void redo() {
        log.atFine().log();
        if (hasNext()) {
            var e = redoStack.removeLast();
            e.redo();
            undoStack.add(e);
        } else {
            Notification notification = new Notification(
                    "操作を進めることができません",
                    "すでに最新の操作のため、これ以上進めることはできません",
                    null,
                    NotificationType.WARN
            );
            Pino.getApp().getService(Publisher.class).publish(notification);
        }
    }
}
