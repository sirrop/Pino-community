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

package jp.gr.java_conf.alpius.pino.ui.conrtols;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;

public class Editor<T> extends Control {
    private ObjectProperty<T> item;

    public final T getItem() {
        return item == null ? null : item.get();
    }

    public final void setItem(T t) {
        itemProperty().set(t);
    }

    public final ObjectProperty<T> itemProperty() {
        if (item == null) {
            item = new SimpleObjectProperty<>(this, "item");
        }
        return item;
    }

    private ObjectProperty<Node> placeholder;

    public final void setPlaceholder(Node placeholder) {
        placeholderProperty().set(placeholder);
    }

    public final Node getPlaceholder() {
        return placeholder == null ? null : placeholder.get();
    }

    public final ObjectProperty<Node> placeholderProperty() {
        if (placeholder == null) {
            placeholder = new SimpleObjectProperty<>(this, "placeholder");
        }
        return placeholder;
    }

    public void refresh() {
        var item = getItem();
        setItem(null);
        setItem(item);
    }
}
