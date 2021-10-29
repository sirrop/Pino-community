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

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.gui.JFxWindow;
import jp.gr.java_conf.alpius.pino.notification.Notification;
import jp.gr.java_conf.alpius.pino.notification.NotificationType;
import jp.gr.java_conf.alpius.pino.notification.Publisher;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.tool.ToolManager;

/**
 * イベントを適切に分配する役割を持つクラスです。
 */
public final class EventDistributor implements Disposable, ToolManager {
    private final Canvas canvas;
    private Tool activeTool;
    private Object lockKey;

    EventDistributor(JFxWindow window) {
        canvas = window.getRootContainer().getCanvas();
        var pane = window.getRootContainer().getCanvasPane();

        pane.setOnMouseClicked(this::mouseClicked);
        pane.setOnMousePressed(this::mousePressed);
        pane.setOnMouseDragged(this::mouseDragged);
        pane.setOnMouseReleased(this::mouseReleased);
        pane.setOnScroll(this::scroll);
        pane.setOnScrollStarted(this::scrollStarted);
        pane.setOnScrollFinished(this::scrollFinished);
    }

    public boolean trySetActiveTool(Tool tool) {
        if (lockKey == null) {
            if (this.activeTool != null) {
                this.activeTool.disable();
            }
            this.activeTool = tool;
            if (tool != null) {
                tool.enable();
            }
            return true;
        } else {
            return false;
        }
    }

    private void mouseClicked(MouseEvent e) {
        activeTool.mouseClicked(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private void mousePressed(MouseEvent e) {
        activeTool.mousePressed(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private void mouseDragged(MouseEvent e) {
        activeTool.mouseDragged(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private void mouseReleased(MouseEvent e) {
        activeTool.mouseReleased(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private void scroll(ScrollEvent e) {
        activeTool.scroll(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private void scrollStarted(ScrollEvent e) {
        activeTool.scrollStart(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private void scrollFinished(ScrollEvent e) {
        activeTool.scrollFinished(asPinoEvent(e.copyFor(canvas, canvas)));
    }

    private static jp.gr.java_conf.alpius.pino.input.MouseEvent asPinoEvent(MouseEvent e) {
        return new jp.gr.java_conf.alpius.pino.input.MouseEvent(e.getSource(), e.getX(), e.getY(), e.getScreenX(), e.getScreenY());
    }

    private static jp.gr.java_conf.alpius.pino.input.ScrollEvent asPinoEvent(ScrollEvent e) {
        return new jp.gr.java_conf.alpius.pino.input.ScrollEvent(e.getSource(), e.getDeltaY());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void activate(Tool tool) {
        var success = trySetActiveTool(tool);
        if (!success) {
            Notification notification = new Notification(
                    "ツールの変更に失敗しました",
                    "ツールは現在ロックされており、変更することが出来ません",
                    null,
                    NotificationType.WARN
            );
            Pino.getApp().getService(Publisher.class).publish(notification);
            return;
        }
        activeTool = tool;
    }

    @Override
    public Tool getActiveTool() {
        return activeTool;
    }

    @Override
    public boolean lock(Object lockKey) {
        if (this.lockKey != null) {
            return false;
        }
        if (lockKey == null) {
            return false;
        }
        this.lockKey = lockKey;
        return true;
    }

    @Override
    public boolean unlock(Object lockKey) {
        if (lockKey == this.lockKey) {
            this.lockKey = null;
            return true;
        }
        return false;
    }
}
