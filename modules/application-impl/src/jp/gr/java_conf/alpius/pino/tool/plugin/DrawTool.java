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

package jp.gr.java_conf.alpius.pino.tool.plugin;

import javafx.scene.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.application.impl.BrushManager;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.brush.BrushContext;
import jp.gr.java_conf.alpius.pino.graphics.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.history.History;
import jp.gr.java_conf.alpius.pino.history.MementoElementBuilder;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.notification.Notification;
import jp.gr.java_conf.alpius.pino.notification.NotificationType;
import jp.gr.java_conf.alpius.pino.notification.Publisher;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;

public class DrawTool implements Tool {
    private static final DrawTool instance = new DrawTool();

    public static DrawTool getInstance() {
        return instance;
    }

    private final Canvas canvas = ((JFxWindow) Pino.getApp().getWindow()).getRootContainer().getCanvas();
    private final double zoomRate = 0.0025;
    private DrawableLayer target;
    private BrushContext context;

    private MementoElementBuilder<DrawableLayer> builder;

    public DrawTool() {
    }

    private boolean isInvisible() {
        var activeModel = Pino.getApp().getProject().getActiveModel();
        if (activeModel == null) {
            throw new IllegalStateException("selection model is null");
        }
        return !activeModel.getActivatedItem().isVisible();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!projectExists()) {
            return;
        }
        if (isInvisible()) {
            Notification notification = new Notification(
                    "非表示のレイヤーへの描画が試みられました",
                    "レイヤーへ描き込みを行う際にはレイヤーを表示してください",
                    null,
                    NotificationType.WARN
            );
            Pino.getApp().getService(Publisher.class).publish(notification);
            return;
        }
        if (context != null) {
            context.dispose();
            context = null;
        }

        var layer = Pino.getApp()
                                    .getProject()
                                    .getActiveModel()
                                    .getActivatedItem();

        if (layer instanceof DrawableLayer drawable) {
            context = BrushManager.getInstance()
                                    .getActiveModel()
                                    .getActivatedItem()
                                    .createContext(drawable);
            builder = MementoElementBuilder.builder(drawable).savePreviousState();
            context.onStart(new DrawEvent(drawable, DrawEvent.Type.ON_START, e.getX(), e.getY()));
            target = drawable;
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (context != null && target != null) {
            context.onDrawing(new DrawEvent(target, DrawEvent.Type.ON_DRAWING, e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (context != null && target != null) {
            context.onFinished(new DrawEvent(target, DrawEvent.Type.ON_FINISHED, e.getX(), e.getY()));
            context.dispose();
            context = null;
            Pino.getApp().getService(History.class).add(builder.saveNextState().build());
            builder = null;
        }
    }

    @Override
    public void scroll(jp.gr.java_conf.alpius.pino.input.ScrollEvent e) {
        double value = e.getDeltaY() * zoomRate;
        canvas.setScaleX(canvas.getScaleX() + value);
        canvas.setScaleY(canvas.getScaleY() + value);
    }

    private boolean projectExists() {
        return Pino.getApp().getProject() != null;
    }

    @Override
    public void dispose() {
    }
}