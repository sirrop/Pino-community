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

package jp.gr.java_conf.alpius.pino.window;

import java.util.EventObject;

public class WindowEvent extends EventObject {
    private final Type type;

    public WindowEvent(Window source, Type type) {
        super(source);
        this.type = type;
    }

    public Window getWindow() {
        return (Window) getSource();
    }

    public Type getEventType() {
        return type;
    }

    public enum Type {
        WINDOW_SHOWN, WINDOW_SHOWING,
        WINDOW_HIDDEN, WINDOW_HIDING,
        WINDOW_CLOSING
    }
}
