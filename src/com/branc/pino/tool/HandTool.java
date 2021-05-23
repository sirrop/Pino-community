package com.branc.pino.tool;

import com.branc.pino.application.ApplicationManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


public class HandTool implements Tool {
    private double x;
    private double y;
    private final Canvas canvas = ApplicationManager.getApp().getRoot().getCanvas();
    private final double zoomRate = ApplicationManager.getApp().getAppConfig().getZoomRate();

    private void init(MouseEvent e) {
        x = e.getSceneX();
        y = e.getSceneY();
    }

    private void move(MouseEvent e) {
        canvas.setTranslateX(canvas.getTranslateX() + e.getSceneX() - x);
        canvas.setTranslateY(canvas.getTranslateY() + e.getSceneY() - y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        init(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        move(e);
        init(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        move(e);
    }

    @Override
    public void scroll(ScrollEvent e) {
        double value = e.getDeltaY() * zoomRate;
        canvas.setScaleX(canvas.getScaleX() + value);
        canvas.setScaleY(canvas.getScaleY() + value);
    }

    @Override
    public void dispose() {

    }
}
