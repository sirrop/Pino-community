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

import java.util.*;

public class LinearGradientBuilder extends PaintBuilder {
    private double startX = 0;
    private double startY = 0;
    private double endX = 1;
    private double endY = 1;
    private boolean relative = true;
    private CycleMethod method = CycleMethod.NONE;
    private List<Stop> stops = new ArrayList<>();

    public LinearGradientBuilder setStartX(double value) {
        this.startX = value;
        return this;
    }

    public LinearGradientBuilder setStartY(double value) {
        this.startY = value;
        return this;
    }

    public LinearGradientBuilder setEndX(double value) {
        this.endX = value;
        return this;
    }

    public LinearGradientBuilder setEndY(double value) {
        this.endY = value;
        return this;
    }

    public LinearGradientBuilder setRelative(boolean value) {
        this.relative = value;
        return this;
    }

    public LinearGradientBuilder setCycleMethod(CycleMethod method) {
        this.method = Objects.requireNonNull(method);
        return this;
    }

    public LinearGradientBuilder addStop(Stop stop) {
        stops.add(stop);
        return this;
    }

    public LinearGradientBuilder addStop(int index, Stop stop) {
        stops.add(index, stop);
        return this;
    }

    public LinearGradientBuilder removeStop(int index) {
        stops.remove(index);
        return this;
    }

    public LinearGradientBuilder removeStop(Stop stop) {
        stops.remove(stop);
        return this;
    }

    public LinearGradientBuilder addStops(Stop... stops) {
        this.stops.addAll(Arrays.asList(stops));
        return this;
    }

    public LinearGradientBuilder addStops(int index, Stop... stops) {
        this.stops.addAll(index, Arrays.asList(stops));
        return this;
    }

    public LinearGradientBuilder removeStops(Stop... stops) {
        this.stops.removeAll(Arrays.asList(stops));
        return this;
    }

    public LinearGradientBuilder clearStops() {
        this.stops.clear();
        return this;
    }

    public LinearGradientBuilder setStops(List<Stop> list) {
        this.stops = list;
        return this;
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Override
    public LinearGradientBuilder setAntialias(boolean antialias) {
        return (LinearGradientBuilder) super.setAntialias(antialias);
    }

    @Override
    public LinearGradientBuilder setComposite(Composite composite) {
        return (LinearGradientBuilder) super.setComposite(composite);
    }

    @Override
    public LinearGradientBuilder setOpacity(float opacity) {
        return (LinearGradientBuilder) super.setOpacity(opacity);
    }

    @Override
    public LinearGradient build() {
        return new LinearGradient(
                isAntialias(), getComposite(), getOpacity(),
                startX, startY,
                endX, endY,
                relative,
                method,
                stops
        );
    }
}
