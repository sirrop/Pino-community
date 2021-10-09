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
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.input.ScrollEvent;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;

public class HandTool implements Tool {
    private static final HandTool instance = new HandTool();

    public static HandTool getInstance() {
        return instance;
    }

    private double x;
    private double y;
    private final Canvas canvas = ((JFxWindow) Pino.getApp().getWindow()).getRootContainer().getCanvas();
    private final double zoomRate = 0.0025;

    private void init(MouseEvent e) {
        x = e.getScreenX();
        y = e.getScreenY();
    }

    private void move(MouseEvent e) {
        canvas.setTranslateX(canvas.getTranslateX() + e.getScreenX() - x);
        canvas.setTranslateY(canvas.getTranslateY() + e.getScreenY() - y);
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