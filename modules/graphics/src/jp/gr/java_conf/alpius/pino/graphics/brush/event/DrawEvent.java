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

package jp.gr.java_conf.alpius.pino.graphics.brush.event;

import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.util.EventObject;

public class DrawEvent extends EventObject {
    public enum Type {
        ON_START,
        ON_DRAWING,
        ON_FINISHED
    }

    private final DrawEvent.Type type;
    private final double x;
    private final double y;

    public DrawEvent(DrawableLayer layer, Type type, double x, double y) {
        super(layer);
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Type getEventType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public DrawableLayer getLayer() {
        return (DrawableLayer) getSource();
    }
}
