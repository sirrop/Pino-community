package com.branc.pino.tool;

import com.branc.pino.application.ApplicationManager;
import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.brush.BrushManager;
import com.branc.pino.paint.layer.Drawable;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.canvas.DrawEventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class DrawTool implements Tool {
    private final Disposable lastDisposable = Disposer.newDisposable();
    private final DrawEventHandler<MouseEvent> handler;
    private final Object lock = new Object();
    private final Canvas canvas = ApplicationManager.getApp().getRoot().getCanvas();
    private final double zoomRate = ApplicationManager.getApp().getAppConfig().getZoomRate();

    public DrawTool() {
        handler = DrawEventHandler.createFxHandler(() -> {
            LayerObject object = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel().getSelectedItem();
            if (object instanceof Drawable) {
                return BrushManager.getInstance().getSelectedBrush().createBrush(((Drawable) object).createGraphics());
            } else {
                return null;
            }
        });
        Disposer.registerDisposable(lastDisposable, handler);
        handler.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ApplicationManager.getApp().getEventDistributor().requestLockActiveTool(lock, this);
        handler.enqueue(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handler.enqueue(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        handler.enqueue(e);
        ApplicationManager.getApp().getEventDistributor().requestUnlockActiveTool(lock);
    }

    @Override
    public void scroll(ScrollEvent e) {
        double value = e.getDeltaY() * zoomRate;
        canvas.setScaleX(canvas.getScaleX() + value);
        canvas.setScaleY(canvas.getScaleY() + value);
    }

    @Override
    public void dispose() {
        Disposer.dispose(lastDisposable);
    }
}
