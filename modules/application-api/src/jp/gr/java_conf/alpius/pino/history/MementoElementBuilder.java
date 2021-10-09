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

package jp.gr.java_conf.alpius.pino.history;

import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.Originator;

public final class MementoElementBuilder<T extends Originator> {
    private final T originator;
    private Memento<?> previousState;
    private Memento<?> nextState;

    private MementoElementBuilder(T originator) {
        this.originator = originator;
    }

    public static <T extends Originator> MementoElementBuilder<T> builder(T originator) {
        return new MementoElementBuilder<>(originator);
    }

    public MementoElementBuilder<T> savePreviousState() {
        previousState = originator.createMemento();
        return this;
    }

    public MementoElementBuilder<T> saveNextState() {
        nextState = originator.createMemento();
        return this;
    }

    public HistoryElement build() {
        return new HistoryElement() {
            @Override
            public void undo() {
                originator.restore(previousState);
            }

            @Override
            public void redo() {
                originator.restore(nextState);
            }
        };
    }

    public HistoryElement build(Runnable action) {
        savePreviousState();
        action.run();
        saveNextState();
        return build();
    }
}
