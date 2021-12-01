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

package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.utils.Checks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class LinearGradient extends Paint {
    private final double startX;
    private final double startY;
    private final double endX;
    private final double endY;
    private final boolean relative;
    private final CycleMethod method;
    private final List<Stop> stops;

    public LinearGradient(
            boolean antialias, Composite composite, float opacity,
            double startX, double startY,
            double endX, double endY,
            boolean relative,
            CycleMethod method,
            Collection<Stop> stops
    ) {
        super(antialias, composite, opacity);
        if (relative) {
            Checks.require(0 <= startX && startX <= 1, relativeErrorMessage("startX"));
            Checks.require(0 <= startY && startY <= 1, relativeErrorMessage("startY"));
            Checks.require(0 <= endX && endX <= 1, relativeErrorMessage("endX"));
            Checks.require(0 <= endY && endY <= 1, relativeErrorMessage("endY"));
        }
        Checks.require(startX <= endX, "startX > endX");
        Checks.require(startY <= endY, "startY > endY");
        Objects.requireNonNull(method, "method == null");
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.relative = relative;
        this.method = method;
        this.stops = listOf(stops);
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public boolean isRelative() {
        return relative;
    }

    public CycleMethod getMethod() {
        return method;
    }

    public List<Stop> getStops() {
        return List.copyOf(stops);
    }

    private static String relativeErrorMessage(String name) {
        return String.format("relative && (%s < 0 || 1 < %s)", name, name);
    }

    private static List<Stop> listOf(Collection<Stop> c) {
        var res = new ArrayList<Stop>();
        if (c == null) return res;
        for (var s: c) {
            if (s != null) {
                res.add(s);
            }
        }
        return res;
    }

    @Override
    public PaintBuilder toBuilder() {
        return new LinearGradientBuilder()
                .setAntialias(isAntialias())
                .setComposite(getComposite())
                .setOpacity(getOpacity())
                .setStartX(startX)
                .setStartY(startY)
                .setEndX(endX)
                .setEndY(endY)
                .setRelative(relative)
                .setCycleMethod(method)
                .setStops(new ArrayList<>(stops));
    }
}
