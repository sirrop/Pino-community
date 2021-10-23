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

package jp.gr.java_conf.alpius.pino.window.impl;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.project.impl.SelectionManager;

import java.awt.geom.IllegalPathStateException;
import java.awt.geom.PathIterator;
import java.util.Collection;
import java.util.List;

// FIXME : キャンバスの拡大率が変化しているときの挙動がおかしいので修正してください
public class SelectionIndicator extends Region implements Disposable {
    private Disposable shapeDisposable;

    public SelectionIndicator(SelectionManager manager) {
        manager.addListener(shape -> {
            if (shapeDisposable != null) shapeDisposable.dispose();
            var child = setShapeDisposable(createFromAwtShape(shape));
            if (child != null) {
                getChildren().setAll(child);
            } else {
                getChildren().clear();
            }
            updateRegion();
        });
    }

    private Shape setShapeDisposable(Shape shape) {
        if (shape == null) {
            shapeDisposable = null;
        } else {
            shapeDisposable = () -> {
                shape.layoutXProperty().unbind();
                shape.layoutYProperty().unbind();
                shape.scaleXProperty().unbind();
                shape.scaleYProperty().unbind();
                shape.translateXProperty().unbind();
                shape.translateYProperty().unbind();
                shape.rotateProperty().unbind();
            };
        }
        return shape;
    }

    private void updateRegion() {

    }

    @Override
    public void dispose() {
        if (shapeDisposable != null) {
            shapeDisposable.dispose();
        }
    }

    private static Shape createFromAwtShape(java.awt.Shape shape) {
        if (shape == null) {
            return null;
        }

        PathIterator itr = shape.getPathIterator(null);
        Path indicator = new Path();

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

        FillRule rule = switch (itr.getWindingRule()) {
            case PathIterator.WIND_EVEN_ODD: yield FillRule.EVEN_ODD;
            case PathIterator.WIND_NON_ZERO: yield FillRule.NON_ZERO;
            default: throw new IllegalPathStateException("Unknown winding rule: " +itr.getWindingRule());
        };
        indicator.setFillRule(rule);
        double[] coords = new double[6];
        while (!itr.isDone()) {
            int segment = itr.currentSegment(coords);
            indicator.getElements().addAll(createPathElement(segment, coords));
            itr.next();
        }
        return indicator;
    }

    private static Collection<PathElement> createPathElement(int segment, double[] coords) {
        return switch (segment) {
            case PathIterator.SEG_CLOSE: yield List.of(new ClosePath());
            case PathIterator.SEG_CUBICTO: yield List.of(new CubicCurveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]));
            case PathIterator.SEG_QUADTO: yield List.of(new QuadCurveTo(coords[0], coords[1], coords[2], coords[3]));
            case PathIterator.SEG_LINETO: yield List.of(new LineTo(coords[0], coords[1]));
            case PathIterator.SEG_MOVETO: yield List.of(new MoveTo(coords[0], coords[1]));
            default: throw new IllegalPathStateException("Unknown segment: " + segment);
        };
    }

    private static RootContainer getRootContainer() {
        return ((JFxWindow) Pino.getApp().getWindow()).getRootContainer();
    }

    private static Canvas getCanvas() {
        return getRootContainer().getCanvas();
    }
}
