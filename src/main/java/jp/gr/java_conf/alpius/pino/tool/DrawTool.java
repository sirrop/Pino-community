package jp.gr.java_conf.alpius.pino.tool;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.brush.Brush;
import jp.gr.java_conf.alpius.pino.brush.BrushManager;
import jp.gr.java_conf.alpius.pino.brush.event.DrawEvent;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.internal.brush.BrushHelper;
import jp.gr.java_conf.alpius.pino.internal.ui.canvas.DrawEventHandlerImpl;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import jp.gr.java_conf.alpius.pino.ui.canvas.DrawEventHandler;
import jp.gr.java_conf.alpius.pino.ui.event.EventType;
import jp.gr.java_conf.alpius.pino.ui.input.MouseButton;

import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;
import static jp.gr.java_conf.alpius.pino.ui.input.MouseEvent.*;

public class DrawTool implements Tool {
    private final Disposable lastDisposable = Disposer.newDisposable();
    private final DrawEventHandler<DrawEvent> handler;
    private final Object lock = new Object();
    private final Canvas canvas = ApplicationManager.getApp().getRoot().getCanvas();
    private final double zoomRate = ApplicationManager.getApp().getAppConfig().getZoomRate();

    public DrawTool() {
        handler = new DrawEventHandlerImpl(
                () -> {
                    LayerObject object = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel().getSelectedItem();
                    if (object instanceof DrawableLayer) {
                        Brush<?> brush = BrushManager.getInstance().getSelectedBrush().createBrush();
                        BrushHelper.setTarget(brush, ((DrawableLayer) object));
                        return brush;
                    } else {
                        return null;
                    }
                }
        );
        Disposer.registerDisposable(lastDisposable, handler);
        handler.start();
    }

    @SuppressWarnings("unchecked")
    private static DrawEvent copyOf(MouseEvent e) {
        final SelectionModel<LayerObject> selectionModel = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel();
        if (selectionModel == null) {
            return null;
        }
        LayerObject layer = selectionModel.getSelectedItem();
        if (!(layer instanceof DrawableLayer)) {
            return null;
        }
        return new DrawEvent(
                getCompatibleMouseButton(e.getButton()),
                getCompatibleEventType((javafx.event.EventType<MouseEvent>) e.getEventType()),
                e.getX(),
                e.getY(),
                ((DrawableLayer) layer)
        );
    }

    private static MouseButton getCompatibleMouseButton(javafx.scene.input.MouseButton button) {
        MouseButton myButton;
        switch (button) {
            case NONE: {
                myButton = MouseButton.NONE;
                break;
            }
            case PRIMARY: {
                myButton = MouseButton.PRIMARY;
                break;
            }
            case MIDDLE: {
                myButton = MouseButton.MIDDLE;
                break;
            }
            case SECONDARY: {
                myButton = MouseButton.SECONDARY;
                break;
            }
            default: {
                myButton = MouseButton.ANY;
                break;
            }
        }
        return myButton;
    }

    public static EventType<jp.gr.java_conf.alpius.pino.ui.input.MouseEvent> getCompatibleEventType(javafx.event.EventType<MouseEvent> type) {
        EventType<jp.gr.java_conf.alpius.pino.ui.input.MouseEvent> res;
        if (type == MouseEvent.MOUSE_CLICKED) {
            res = jp.gr.java_conf.alpius.pino.ui.input.MouseEvent.CLICKED;
        } else if (type == MouseEvent.MOUSE_PRESSED) {
            res = jp.gr.java_conf.alpius.pino.ui.input.MouseEvent.PRESSED;
        } else if (type == MouseEvent.MOUSE_DRAGGED) {
            res = DRAGGED;
        } else if (type == MOUSE_RELEASED) {
            res = RELEASED;
        } else {
            res = ANY;
        }
        return res;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (projectExists()) {
            ApplicationManager.getApp().getEventDistributor().requestLockActiveTool(lock, this);
            handler.enqueue(copyOf(e));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (projectExists()) {
            handler.enqueue(copyOf(e));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (projectExists()) {
            handler.enqueue(copyOf(e));
            ApplicationManager.getApp().getEventDistributor().requestUnlockActiveTool(lock);
        }
    }

    @Override
    public void scroll(ScrollEvent e) {
        double value = e.getDeltaY() * zoomRate;
        canvas.setScaleX(canvas.getScaleX() + value);
        canvas.setScaleY(canvas.getScaleY() + value);
    }

    private boolean projectExists() {
        return ProjectManager.getInstance().getProject() != null;
    }


    @Override
    public void dispose() {
        Disposer.dispose(lastDisposable);
    }
}
