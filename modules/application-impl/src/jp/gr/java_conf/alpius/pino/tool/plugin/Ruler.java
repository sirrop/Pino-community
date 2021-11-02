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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.gui.tools.RulerIndicator;
import jp.gr.java_conf.alpius.pino.input.MouseEvent;
import jp.gr.java_conf.alpius.pino.input.ScrollEvent;
import jp.gr.java_conf.alpius.pino.tool.Tool;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Rulerは、直線になるようDrawEventを補正してDrawToolに渡すツールです。
 * <p>
 *     補正の仕方は次の3つあります。
 *     <ul>
 *         <li>HORIZONTAL: 直線上のy座標が同じ点に補正する</li>
 *         <li>VERTICAL: 直線上のx座標が同じ点に補正する</li>
 *         <li>SHORTEST: 直線上の最も近い点に補正する</li>
 *     </ul>
 * </p>
 */
public class Ruler implements Tool {
    private Mode mode = Mode.VERTICAL;
    private final DoubleProperty slope = new SimpleDoubleProperty(this, "slope", 1);
    private Line line;
    private final RulerIndicator indicator = new RulerIndicator();

    public Ruler() {
        indicator.slopeProperty().bind(slope);
    }

    public DoubleProperty slopeProperty() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope.set(slope);
    }

    public double getSlope() {
        return slope.get();
    }

    public void setMode(Mode mode) {
        this.mode = Objects.requireNonNull(mode);
    }

    public Mode getMode() {
        return mode;
    }

    private MouseEvent filter(MouseEvent e) {
        return switch (mode) {
            case HORIZONTAL: yield e.withX(line.invert(e.getY()));
            case VERTICAL: yield e.withY(line.apply(e.getX()));
            case SHORTEST: {
                var p = line.shortest(e.getX(), e.getY());
                yield e.withX(p.x).withY(p.y);
            }
        };
    }

    @Override
    public void mousePressed(MouseEvent e) {
        line = line(getSlope(), e.getY() - getSlope() * e.getX());
        DrawTool.getInstance().mousePressed(filter(e));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        DrawTool.getInstance().mouseDragged(filter(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        DrawTool.getInstance().mouseReleased(filter(e));
    }

    @Override
    public void scroll(ScrollEvent e) {
        DrawTool.getInstance().scroll(e);
    }

    @Override
    public void enable() {
        Pino.getApp()
                .getWindow()
                .getRootContainer()
                .getCanvasPane()
                .getChildren()
                .add(indicator);
    }

    @Override
    public void disable() {
        Pino.getApp()
                .getWindow()
                .getRootContainer()
                .getCanvasPane()
                .getChildren()
                .remove(indicator);
    }


    public enum Mode {
        HORIZONTAL, VERTICAL, SHORTEST
    }

    private interface Line {
        double apply(double x);
        double invert(double y);
        Point2D.Double shortest(double x, double y);
    }

    private static Line line(double a, double b) {
        if (a == 0) {
            throw new IllegalArgumentException();
        }
        return new Line() {

            @Override
            public double apply(double x) {
                return a * x + b;
            }

            @Override
            public double invert(double y) {
                return (y - b) / a;
            }

            @Override
            public Point2D.Double shortest(double x, double y) {
                double _x = (x + a * y - a * b) / (a * a + 1);
                return new Point2D.Double(_x, apply(_x));
            }
        };
    }
}
