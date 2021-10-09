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

package jp.gr.java_conf.alpius.pino.application.impl;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.brush.Eraser;
import jp.gr.java_conf.alpius.pino.graphics.brush.Pencil;
import jp.gr.java_conf.alpius.pino.util.ActiveModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;

public final class BrushManager {
    public static BrushManager getInstance() {
        return Pino.getApp().getService(BrushManager.class);
    }

    public BrushManager() {
        activeModel.activate(0);
    }

    private final ObservableList<Brush> brushList = FXCollections.observableArrayList(new Pencil(), new Eraser());
    private final ActiveModel<Brush> activeModel = new BrushMgrActiveModel(this);

    public List<Brush> getBrushList() {
        return brushList;
    }

    private ObservableList<Brush> getObservableList() {
        return brushList;
    }

    public ActiveModel<Brush> getActiveModel() {
        return activeModel;
    }

    private static class BrushMgrActiveModel implements ActiveModel<Brush> {
        private final BrushManager mgr;
        private Brush activated;
        private final List<IntConsumer> listeners = new ArrayList<>();

        public BrushMgrActiveModel(BrushManager mgr) {
            this.mgr = Objects.requireNonNull(mgr);
            ListChangeListener<Brush> childrenObserver = c -> {
                while (c.next()) {
                    var from = c.getFrom();

                    if (c.wasReplaced() || c.getAddedSize() == getItemCount()) {
                        updateDefaultActivation();
                        return;
                    }

                    if (getActivatedIndex() == -1 || from > getActivatedIndex()) {
                        return;
                    }

                    c.reset();
                    boolean added = false;
                    boolean removed = false;
                    int addedSize = 0;
                    int removedSize = 0;
                    while (c.next()) {
                        added |= c.wasAdded();
                        removed |= c.wasRemoved();
                        addedSize += c.getAddedSize();
                        removedSize += c.getRemovedSize();
                    }

                    if (added && !removed) {
                        activate(Math.min(getItemCount() - 1, getActivatedIndex() + addedSize));
                    } else if (!added && removed) {
                        activate(Math.max(0, getActivatedIndex() - removedSize));
                    }
                }
            };
            mgr.getObservableList().addListener(childrenObserver);
            updateDefaultActivation();
        }

        private void updateDefaultActivation() {
            if (isEmpty()) {
                activate(-1);
            } else {
                activate(0);
            }
        }

        private int getItemCount() {
            return mgr.brushList.size();
        }

        private boolean isEmpty() {
            return mgr.brushList.isEmpty();
        }

        private Brush getBrush(int index) {
            if (isEmpty()) return null;
            if (index < 0 || index >= getItemCount()) return null;
            return mgr.brushList.get(index);
        }

        @Override
        public void addListener(IntConsumer listener) {
            listeners.add(listener);
        }

        @Override
        public void removeListener(IntConsumer listener) {
            listeners.remove(listener);
        }

        @Override
        public void activate(int index) {
            if (index < 0 || index >= getItemCount()) {
                activated = null;
                return;
            }
            activated = getBrush(index);

            for (IntConsumer listener: listeners) {
                listener.accept(index);
            }
        }

        @Override
        public void activateNext() {
            int next = getActivatedIndex() + 1;
            if (next == getItemCount()) {
                return;
            }
            activate(next);
        }

        @Override
        public void activatePrevious() {
            int previous = getActivatedIndex() - 1;
            if (previous < 0) {
                return;
            }
            activate(previous);
        }

        @Override
        public int getActivatedIndex() {
            return mgr.brushList.indexOf(activated);
        }

        @Override
        public Brush getActivatedItem() {
            return activated;
        }

        @Override
        public boolean isActivated(int index) {
            return getActivatedIndex() == index;
        }
    }
}
