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

package jp.gr.java_conf.alpius.pino.input;

import java.io.Serial;

public class ScrollEvent extends InputEvent {
    @Serial
    private static final long serialVersionUID = 8818202422570575914L;

    private final double x;
    private final double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public boolean isCtrlDown() {
        return ctrlDown;
    }

    public boolean isAltDown() {
        return altDown;
    }

    public boolean isMetaDown() {
        return metaDown;
    }

    private final double deltaX;
    private final double deltaY;
    private final boolean shiftDown;
    private final boolean ctrlDown;
    private final boolean altDown;
    private final boolean metaDown;

    public ScrollEvent(Object source, double x, double y, double deltaX, double deltaY,
                       boolean shiftDown, boolean ctrlDown, boolean altDown, boolean metaDown) {
        super(source);
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.shiftDown = shiftDown;
        this.ctrlDown = ctrlDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
    }

    public double getDeltaY() {
        return deltaY;
    }
}
