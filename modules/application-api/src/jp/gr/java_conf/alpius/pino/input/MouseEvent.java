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

public class MouseEvent extends InputEvent {
    private final double x;
    private final double y;
    private final double screenX;
    private final double screenY;

    public MouseEvent(Object source, double x, double y, double screenX, double screenY) {
        super(source);
        this.x = x;
        this.y = y;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }
}
