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

package jp.gr.java_conf.alpius.pino.project.impl;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.graphics.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.Layers;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.service.MutableServiceContainer;
import jp.gr.java_conf.alpius.pino.service.SimpleServiceContainer;
import jp.gr.java_conf.alpius.pino.util.ActiveModel;
import jp.gr.java_conf.alpius.pino.util.Key;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.util.*;
import java.util.function.IntConsumer;

public class PinoProject implements Project {
    private static final boolean DEFAULT_ACCELERATED = false;
    private static final ICC_Profile DEFAULT_PROFILE = ICC_Profile.getInstance(ColorSpace.CS_sRGB);

    private final Canvas canvas;
    private final ObservableList<LayerObject> children = FXCollections.observableArrayList();
    private final ICC_Profile profile;

    private MutableServiceContainer container;
    private Map<Key<?>, Object> userData;

    private final ActiveModel<LayerObject> activeModel;


    /**
     * デフォルト設定でプロジェクトを構築します。
     * @param width 幅
     * @param height 高さ
     * @throws IllegalArgumentException widthかheightが0以下の場合
     */
    public PinoProject(int width, int height) {
        this(width, height, DEFAULT_ACCELERATED);
    }

    public PinoProject(int width, int height, boolean accelerated) {
        this(width, height, DEFAULT_PROFILE, accelerated);
    }

    public PinoProject(int width, int height, ICC_Profile profile) {
        this(width, height, profile, DEFAULT_ACCELERATED);
    }

    public PinoProject(int width, int height, ICC_Profile profile, boolean accelerated) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width <= 0 || height <= 0");
        }
        if (accelerated) {
            canvas = Canvas.createAccelerated();
        } else {
            canvas = Canvas.createGeneral();
        }

        canvas.setSize(width, height);
        activeModel = new ProjectActiveModel(this);
        this.profile = Objects.requireNonNull(profile);
        activeModel.activate(0);
        getChildren().addAll(Layers.create(DrawableLayer::new, getWidth(), getHeight()));
    }

    @Override
    public int getWidth() {
        return canvas.getWidth();
    }

    @Override
    public int getHeight() {
        return canvas.getHeight();
    }

    @Override
    public ICC_Profile getProfile() {
        return profile;
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public List<LayerObject> getLayers() {
        return children;
    }

    protected ObservableList<LayerObject> getChildren() {
        return children;
    }

    @Override
    public ActiveModel<LayerObject> getActiveModel() {
        return activeModel;
    }

    private MutableServiceContainer getContainer() {
        if (container == null) {
            container = new SimpleServiceContainer();
        }
        return container;
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return getContainer().getService(serviceClass);
    }

    @Override
    public void dispose() {
        canvas.dispose();
        children.clear();
        container = null;
    }

    private Map<Key<?>, Object> getUserDataHolder() {
        if (userData == null) {
            userData = new HashMap<>();
        }
        return userData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getUserData(Key<T> key) {
        return (T) getUserDataHolder().get(key);
    }

    @Override
    public <T> void putUserData(Key<T> key, T value) {
        getUserDataHolder().put(key, value);
    }

    // thread unsafe
    private static class ProjectActiveModel implements ActiveModel<LayerObject> {
        private final PinoProject project;
        private LayerObject activated;
        private final List<IntConsumer> listeners = new ArrayList<>();

        public ProjectActiveModel(PinoProject project) {
            this.project = Objects.requireNonNull(project);
            ListChangeListener<LayerObject> childrenObserver = c -> {
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
            project.getChildren().addListener(childrenObserver);
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
            return project.getChildren().size();
        }

        private boolean isEmpty() {
            return project.getChildren().isEmpty();
        }

        private LayerObject getLayerObject(int index) {
            if (isEmpty()) return null;
            if (index < 0 || index >= getItemCount()) return null;
            return project.getChildren().get(index);
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
            activated = getLayerObject(index);

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
            return project.getChildren().indexOf(activated);
        }

        @Override
        public LayerObject getActivatedItem() {
            return activated;
        }

        @Override
        public boolean isActivated(int index) {
            return getActivatedIndex() == index;
        }
    }
}
