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

package jp.gr.java_conf.alpius.pino.graphics.brush;

import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.disposable.Disposer;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;

public interface Brush {
    String getName();
    void setName(String name);
    List<PropertyDescriptor> getUnmodifiablePropertyList();
    void addListener(PropertyChangeListener listener);
    default void addListener(PropertyChangeListener listener, Disposable parent) {
        addListener(listener);
        Disposer.registerDisposable(parent, () -> removeListener(listener));
    }
    void removeListener(PropertyChangeListener listener);
    BrushContext createContext(DrawableLayer target);
    default <R> R accept(BrushVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
