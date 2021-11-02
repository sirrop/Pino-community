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

package jp.gr.java_conf.alpius.pino.gui.tools;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jp.gr.java_conf.alpius.pino.gui.tools.skin.RulerIndicatorSkin;

public class RulerIndicator extends Control {
    private static final double DEFAULT_SLOPE = 1;

    private DoubleProperty slope;

    public final DoubleProperty slopeProperty() {
        if (slope == null) {
            slope = new SimpleDoubleProperty(this, "slope", DEFAULT_SLOPE);
        }
        return slope;
    }

    public final void setSlope(double value) {
        slopeProperty().set(value);
    }

    public final double getSlope() {
        return slope == null ? DEFAULT_SLOPE : slope.get();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RulerIndicatorSkin(this);
    }
}
