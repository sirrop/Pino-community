package com.branc.pino.application;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.tool.DrawTool;
import com.branc.pino.tool.HandTool;
import com.branc.pino.tool.Tool;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.Objects;

public class EventDistributor implements Disposable {
    private final Disposable lastDispose = Disposer.newDisposable();

    private final Tool drawTool;
    private final Tool handTool;

    private Tool activeTool;
    private Object lockKey;

    EventDistributor() {
        var canvas = ApplicationManager.getApp().getRoot().getCanvas();
        var pane = ApplicationManager.getApp().getRoot().getCanvasPane();
        drawTool = new DrawTool();
        handTool = new HandTool();
        activeTool = drawTool;
        Disposer.registerDisposable(lastDispose, drawTool);

        canvas.setOnMouseClicked(this::mouseClicked);
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
        canvas.setOnScroll(this::scroll);
        canvas.setOnScrollStarted(this::scrollStarted);
        canvas.setOnScrollFinished(this::scrollFinished);

        pane.setOnMouseClicked(this::mouseClicked);
        pane.setOnMousePressed(this::mousePressed);
        pane.setOnMouseDragged(this::mouseDragged);
        pane.setOnMouseReleased(this::mouseReleased);
        pane.setOnScroll(this::scroll);
        pane.setOnScrollStarted(this::scrollStarted);
        pane.setOnScrollFinished(this::scrollFinished);
    }

    public void activateDrawTool() {
        trySetActiveTool(drawTool);
    }

    public void activateHandTool() {
        trySetActiveTool(handTool);
    }

    public boolean trySetActiveTool(Tool tool) {
        if (lockKey == null) {
            this.activeTool = tool;
            return true;
        } else {
            return false;
        }
    }

    public void requestLockActiveTool(Object lockKey, Tool tool) {
        if (tool == activeTool) this.lockKey = Objects.requireNonNull(lockKey);
    }

    public void requestUnlockActiveTool(Object lockKey) {
        if (this.lockKey == lockKey) this.lockKey = null;
    }

    private void mouseClicked(MouseEvent e) {
        activeTool.mouseClicked(e);
    }

    private void mousePressed(MouseEvent e) {
        activeTool.mousePressed(e);
    }

    private void mouseDragged(MouseEvent e) {
        activeTool.mouseDragged(e);
    }

    private void mouseReleased(MouseEvent e) {
        activeTool.mouseReleased(e);
    }

    private void scroll(ScrollEvent e) {
        activeTool.scroll(e);
    }

    private void scrollStarted(ScrollEvent e) {
        activeTool.scrollStart(e);
    }

    private void scrollFinished(ScrollEvent e) {
        activeTool.scrollFinished(e);
    }

    public void dispose() {
        Disposer.dispose(lastDispose);
    }
}
