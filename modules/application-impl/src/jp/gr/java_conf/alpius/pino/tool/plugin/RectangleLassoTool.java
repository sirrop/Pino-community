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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.project.impl.SelectionManager;
import jp.gr.java_conf.alpius.pino.tool.Tool;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;
import jp.gr.java_conf.alpius.pino.window.impl.RootContainer;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

// FIXME : キャンバスの拡大率が変化しているときの挙動がおかしいので修正してください
public class RectangleLassoTool implements Tool {
    private static final int INDEX_LEFT = 0;
    private static final int INDEX_TOP = 1;
    private static final int INDEX_WIDTH = 2;
    private static final int INDEX_HEIGHT = 3;

    private double startX, startY, endX, endY;

    @Override
    public void mousePressed(MouseEvent e) {
        Optional.ofNullable(Pino.getApp().getProject())
                .ifPresent(it -> it.getService(SelectionManager.class).set(null));
        startX = endX = e.getX();
        startY = endY = e.getY();
        addIndicator();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        updateIndicator();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        removeIndicator();
        Optional.ofNullable(Pino.getApp().getProject())
                .ifPresent(it -> it.getService(SelectionManager.class)
                        .updateAndGet(shape -> createRectangle()));
    }

    private Rectangle indicator;

    private void addIndicator() {
        indicator = new Rectangle();
        var canvas = getCanvas();
        indicator.layoutXProperty().bind(canvas.layoutXProperty());
        indicator.layoutYProperty().bind(canvas.layoutYProperty());
        indicator.scaleXProperty().bind(canvas.scaleXProperty());
        indicator.scaleYProperty().bind(canvas.scaleYProperty());
        indicator.translateXProperty().bind(canvas.translateXProperty());
        indicator.translateYProperty().bind(canvas.translateYProperty());
        indicator.rotateProperty().bind(canvas.rotateProperty());

        // FIXME : これらのパラメータをユーザーが変更できるようにしてください
        indicator.getStrokeDashArray().setAll(3d, 5d);
        indicator.setStrokeWidth(1);
        indicator.setStroke(Color.web("#222"));
        indicator.setFill(Color.TRANSPARENT);

        getCanvasPane().getChildren().add(indicator);
    }

    private void updateIndicator() {
        double[] coord = getRectCoord();

        indicator.setX(coord[INDEX_LEFT]);
        indicator.setY(coord[INDEX_TOP]);
        indicator.setWidth(coord[INDEX_WIDTH]);
        indicator.setHeight(coord[INDEX_HEIGHT]);
    }

    private void removeIndicator() {
        indicator.layoutXProperty().unbind();
        indicator.layoutYProperty().unbind();
        indicator.scaleXProperty().unbind();
        indicator.scaleYProperty().unbind();
        indicator.translateXProperty().unbind();
        indicator.translateYProperty().unbind();
        indicator.rotateProperty().unbind();
        getCanvasPane().getChildren().remove(indicator);
        indicator = null;
    }

    private double[] getRectCoord() {
        double[] res = new double[4];
        if (startX > endX) {
            res[INDEX_LEFT] = endX;
            res[INDEX_WIDTH] = startX - endX;
        } else {
            res[INDEX_LEFT] = startX;
            res[INDEX_WIDTH] = endX - startX;
        }
        if (startY > endY) {
            res[INDEX_TOP] = endY;
            res[INDEX_HEIGHT] = startY - endY;
        } else {
            res[INDEX_TOP] = startY;
            res[INDEX_HEIGHT] = endY -startY;
        }
        return res;
    }

    private Rectangle2D.Double createRectangle() {
        double[] coord = getRectCoord();
        return new Rectangle2D.Double(coord[INDEX_LEFT], coord[INDEX_TOP], coord[INDEX_WIDTH], coord[INDEX_HEIGHT]);
    }

    private static RootContainer getRootContainer() {
        return ((JFxWindow) Pino.getApp().getWindow()).getRootContainer();
    }

    private static Pane getCanvasPane() {
        return getRootContainer().getCanvasPane();
    }

    private static Canvas getCanvas() {
        return getRootContainer().getCanvas();
    }

}
