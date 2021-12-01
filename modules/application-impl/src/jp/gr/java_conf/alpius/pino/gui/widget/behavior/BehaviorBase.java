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

package jp.gr.java_conf.alpius.pino.gui.widget.behavior;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;

public class BehaviorBase<N extends Node> implements Disposable {
    private final N node;

    public BehaviorBase(N node) {
        this.node = node;
    }

    public N getNode() {
        return node;
    }

    public <E extends Event> void addEventHandler(EventType<E> type, EventHandler<E> handler) {
        node.addEventHandler(type, handler);
    }

    public <E extends Event> void addEventFilter(EventType<E> type, EventHandler<E> handler) {
        node.addEventFilter(type, handler);
    }

    @Override
    public void dispose() {

    }
}
