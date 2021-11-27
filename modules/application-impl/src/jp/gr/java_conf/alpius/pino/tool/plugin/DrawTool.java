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

import com.google.common.flogger.FluentLogger;
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
import jp.gr.java_conf.alpius.pino.project.impl.SelectionManager;
import jp.gr.java_conf.alpius.pino.tool.Tool;

public class DrawTool implements Tool {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private static final DrawTool instance = new DrawTool();

    public static DrawTool getInstance() {
        return instance;
    }

    private static final double DEFAULT_ZOOM_RATE = 0.0025;

    private final Canvas canvas = Pino.getApp().getWindow().getRootContainer().getCanvas();
    private final double zoomRate = DEFAULT_ZOOM_RATE;
    private DrawableLayer target;
    private BrushContext context;

    /** 最後に描画されたスクリーン上の点 */
    private final double[] vec2 = new double[2];

    /** 補完が有効になる距離 */
    private static final double D = 5;

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

    private void setPoint(double x, double y) {
        vec2[0] = x;
        vec2[1] = y;
    }

    private double getDistance(double x, double y) {
        var _x = x - vec2[0];
        var _y = y - vec2[1];
        return Math.sqrt(_x * _x + _y * _y);
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
            if (drawable.isLocked()) notifyLocked();
            setPoint(e.getScreenX(), e.getScreenY());
            var context = this.context = BrushManager.getInstance()
                                    .getActiveModel()
                                    .getActivatedItem()
                                    .createContext(drawable);
            var selection = Pino.getApp().getProject().getService(SelectionManager.class).get();
            context.clip(selection);
            builder = MementoElementBuilder.builder(drawable).savePreviousState();
            runAsync(() -> context.onStart(new DrawEvent(drawable, DrawEvent.Type.ON_START, e.getX(), e.getY())));
            target = drawable;
        }

    }

    /* FIXME:
     * この補完は線形です。急な方向転換のには対応できません。
     */
    private void complementIfNeed(MouseEvent e) {
        /* Canvas上の点XY */
        double localX = e.getX();
        double localY = e.getY();

        /* Screen上の点XY */
        double[] pointInScreen = new double[2];
        pointInScreen[0] = e.getScreenX();
        pointInScreen[1] = e.getScreenY();

        /* 前回描画した点からScreen上の点までの距離 */
        double d;
        while ((d = getDistance(pointInScreen[0], pointInScreen[1])) >= D) {
            var coeff = D / d;

            /* 補完された座標 */
            double[] vec = {
                    (pointInScreen[0] - vec2[0]) * coeff + vec2[0],
                    (pointInScreen[1] - vec2[1]) * coeff + vec2[1]
            };

            vec2[0] = vec[0];
            vec2[1] = vec[1];
            var context = this.context;
            var target = this.target;
            runAsync(() -> context.onDrawing(new DrawEvent(target, DrawEvent.Type.ON_DRAWING, localX - pointInScreen[0] + vec[0], localY - pointInScreen[1] + vec[1])));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (context != null && target != null) complementIfNeed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (context != null && target != null) {
            complementIfNeed(e);
            var context = this.context;
            var builder = this.builder;
            var target = this.target;
            runAsync(() -> {
                context.onFinished(new DrawEvent(target, DrawEvent.Type.ON_FINISHED, e.getX(), e.getY()));
                context.dispose();
                Pino.getApp().getService(History.class).add(builder.saveNextState().build());
                repaint();
            });
            this.context = null;
            this.builder = null;
        }
    }

    @Override
    public void scroll(jp.gr.java_conf.alpius.pino.input.ScrollEvent e) {
        if (projectExists()) {
            if (e.isCtrlDown()) {
                var value = (e.getDeltaY() + e.getDeltaX()) * zoomRate;
                canvas.setScaleX(canvas.getScaleX() + value);
                canvas.setScaleY(canvas.getScaleY() + value);
            } else {
                var x = -e.getDeltaX();
                var y = -e.getDeltaY();
                canvas.setTranslateX(canvas.getTranslateX() + x);
                canvas.setTranslateY(canvas.getTranslateY() + y);
            }
        }
    }

    private void runAsync(Runnable command) {
        Pino.getApp().runLater(command);
    }

    private static void repaint() {
        var app = Pino.getApp();
        app.getWindow().getRootContainer().getLayerView().refresh();
    }

    private boolean projectExists() {
        return Pino.getApp().getProject() != null;
    }

    private void notifyLocked() {
        Notification notification = new Notification(
                "レイヤーがロックされています", /* title */
                "ロックされたレイヤーに描画することは出来ません\nロックを解除するか、ロックされていない描画レイヤを選択してください", /* body */
                null,  /* icon */
                NotificationType.INFO  /* type */
        );
        Pino.getApp().getService(Publisher.class).publish(notification);
    }

    @Override
    public void dispose() {
    }
}