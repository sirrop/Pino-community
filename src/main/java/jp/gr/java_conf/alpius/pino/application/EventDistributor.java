package jp.gr.java_conf.alpius.pino.application;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;
import jp.gr.java_conf.alpius.pino.tool.DrawTool;
import jp.gr.java_conf.alpius.pino.tool.HandTool;
import jp.gr.java_conf.alpius.pino.tool.Tool;

import java.util.Objects;

public class EventDistributor implements Disposable {
    private final Disposable lastDispose = Disposer.newDisposable();
    private final Canvas canvas;
    private final Tool drawTool;
    private final Tool handTool;

    private Tool activeTool;
    private Object lockKey;

    EventDistributor() {
        canvas = ApplicationManager.getApp().getRoot().getCanvas();
        var pane = ApplicationManager.getApp().getRoot().getCanvasPane();
        drawTool = new DrawTool();
        handTool = new HandTool();
        activeTool = handTool;
        Disposer.registerDisposable(lastDispose, drawTool);

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
        activeTool.mouseClicked(e.copyFor(canvas, canvas));
    }

    private void mousePressed(MouseEvent e) {
        activeTool.mousePressed(e.copyFor(canvas, canvas));
    }

    private void mouseDragged(MouseEvent e) {
        activeTool.mouseDragged(e.copyFor(canvas, canvas));
    }

    private void mouseReleased(MouseEvent e) {
        activeTool.mouseReleased(e.copyFor(canvas, canvas));
    }

    private void scroll(ScrollEvent e) {
        activeTool.scroll(e.copyFor(canvas, canvas));
    }

    private void scrollStarted(ScrollEvent e) {
        activeTool.scrollStart(e.copyFor(canvas, canvas));
    }

    private void scrollFinished(ScrollEvent e) {
        activeTool.scrollFinished(e.copyFor(canvas, canvas));
    }

    public void dispose() {
        Disposer.dispose(lastDispose);
    }
}
