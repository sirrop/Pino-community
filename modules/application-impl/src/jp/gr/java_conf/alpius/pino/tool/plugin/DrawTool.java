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
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
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

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrawTool implements Tool {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private static final DrawTool instance = new DrawTool();

    public static DrawTool getInstance() {
        return instance;
    }

    private static final double DEFAULT_ZOOM_RATE = 0.0025;

    private final RenderEngine engine = new RenderEngine();
    private final Canvas canvas = Pino.getApp().getWindow().getRootContainer().getCanvas();
    private final double zoomRate = DEFAULT_ZOOM_RATE;

    /** 最後に描画されたスクリーン上の点 */
    private final double[] vec2 = new double[2];

    /** 補完が有効になる距離 */
    private double distance = 5;

    public void setDistance(double value) {
        if (value < 1) {
            throw new IllegalArgumentException();
        }
        distance = value;
    }

    public double getDistance() {
        return distance;
    }

    private MementoElementBuilder<DrawableLayer> builder;

    public DrawTool() {
        engine.setOnFinishing(() -> {
            Pino.getApp().getService(History.class).add(builder.saveNextState().build());
            repaint();
        });
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

        var layer = Pino.getApp()
                                    .getProject()
                                    .getActiveModel()
                                    .getActivatedItem();

        if (layer instanceof DrawableLayer drawable) {
            if (drawable.isLocked()) notifyLocked();
            setPoint(e.getScreenX(), e.getScreenY());
            var context = BrushManager.getInstance()
                                    .getActiveModel()
                                    .getActivatedItem()
                                    .createContext(drawable);
            var selection = Pino.getApp().getProject().getService(SelectionManager.class).get();
            context.clip(selection);
            builder = MementoElementBuilder.builder(drawable).savePreviousState();
            engine.start(new RenderContext(context, drawable));
            engine.queue.addLast(new Command(DrawEvent.Type.ON_START, e.getX(), e.getY()));
        }

    }

    /* FIXME:
     * この補間は線形です。急な方向転換には対応できません。
     * また、まれに補間が完全でなく、線が途切れる不具合があるようです。
     * この不具合はアプリの動作が重くなった場合に顕著であり、プロジェクトのサイズを大きくすると頻度が多くなる傾向があります
     */
    private void complementIfNeed(MouseEvent e) {
        /* Canvas上の点XY */
        double localX = e.getX();
        double localY = e.getY();

        /* Screen上の点XY */
        var screenX = e.getScreenX();
        var screenY = e.getScreenY();

        var threshold = getDistance();

        /* 前回描画した点からScreen上の点までの距離 */
        double d;
        while ((d = getDistance(screenX, screenY)) >= threshold) {
            var coeff = distance / d;

            /* 補間された座標 */
            double[] vec = {
                    (screenX - vec2[0]) * coeff + vec2[0],
                    (screenY - vec2[1]) * coeff + vec2[1]
            };

            System.arraycopy(vec, 0, vec2, 0, 2);

            engine.queue.addLast(new Command(DrawEvent.Type.ON_DRAWING, localX - screenX + vec[0], localY - screenY + vec[1]));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        complementIfNeed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        complementIfNeed(e);
        engine.queue.addLast(new Command(DrawEvent.Type.ON_FINISHED, e.getX(), e.getY()));
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
        engine.cancel();
    }

    private static class RenderEngine {
        private Thread runner;
        public final BlockingDeque<Command> queue;
        private RenderContext renderingContext;
        private final AtomicBoolean running = new AtomicBoolean(false);
        private Runnable onFinishing;

        public RenderEngine() {
            queue = new LinkedBlockingDeque<>();
        }

        public void start(RenderContext context) {
            Objects.requireNonNull(context, "context == null");
            cancel();
            renderingContext = context;
            runner = new Thread(() -> {
                running.set(true);
                boolean started = false;
                loop:
                while (true) {
                    try {
                        var e  = queue.takeFirst().newEvent(context.drawable);
                        switch (e.getEventType()) {
                            case ON_START -> {
                                if (started) {
                                    break loop;
                                }
                                started = true;
                                context.brush.onStart(e);
                            }
                            case ON_DRAWING -> context.brush.onDrawing(e);
                            case ON_FINISHED -> {
                                context.brush.onFinished(e);
                                context.dispose();
                                break loop;
                            }
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
                context.dispose();
                finish();
            });
            runner.start();
        }

        public void setOnFinishing(Runnable action) {
            onFinishing = action;
        }

        public void finish() {
            var action = onFinishing;
            if (action != null && running.getAndSet(false)) {
                action.run();
            }
        }

        public void cancel() {
            finish();
            if (runner != null) {
                runner.interrupt();
            }
            if (renderingContext != null) {
                renderingContext.dispose();
            }
        }
    }

    private static class RenderContext implements Disposable {
        private static final BrushContext NOP = new BrushContext() {
            @Override
            public void clip(Shape shape) {

            }

            @Override
            public void setClip(Shape shape) {

            }

            @Override
            public Shape getClip() {
                return null;
            }

            @Override
            public void onStart(DrawEvent e) {

            }

            @Override
            public void onDrawing(DrawEvent e) {

            }

            @Override
            public void onFinished(DrawEvent e) {

            }

            @Override
            public void dispose() {

            }
        };
        private BrushContext brush;
        private final DrawableLayer drawable;

        public RenderContext(BrushContext brush, DrawableLayer drawable) {
            this.brush = Objects.requireNonNull(brush);
            this.drawable = Objects.requireNonNull(drawable);
        }

        public void dispose() {
            brush.dispose();
            brush = NOP;
        }
    }

    private static record Command(DrawEvent.Type type, double x, double y) {
        public Command(DrawEvent.Type type, double x, double y) {
            this.type = Objects.requireNonNull(type);
            this.x = x;
            this.y = y;
        }

        public DrawEvent newEvent(DrawableLayer layer) {
            return new DrawEvent(layer, type, x, y);
        }
    }
}