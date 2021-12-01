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

package jp.gr.java_conf.alpius.pino.graphics.layer;

import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class ParentBase extends LayerObject implements Parent {
    private static final Consumer<LayerObject> MARK_DIRTY = layer -> layer.depthManager.markDirty();
    private static final Consumer<LayerObject> CLEAR = layer -> layer.depthManager.clear();

    private final LayerList children;

    public ParentBase(List<LayerObject> children) {
        this.children = new ParentLayerList(this, children);
    }

    @Override
    void setParentPriv(Parent parent) {
        Consumer<LayerObject> action;
        if (parent == null) {
            action = CLEAR;
        } else {
            action = MARK_DIRTY;
        }
        for (var child: children) {
            action.accept(child);
        }
    }

    public LayerList getChildren() {
        return children;
    }

    protected void layoutChildren() {

    }

    @Override
    final void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough) {
        Rectangle aoiRect = aoi.getBounds();
        var x = (int) getX();
        var y = (int) getY();
        var maxX = aoiRect.x + aoiRect.width;
        var maxY = aoiRect.y + aoiRect.height;

        BufferedImage renderedImage = new BufferedImage(maxX - x, maxY - y, BufferedImage.TYPE_INT_ARGB_PRE);
        var g2d = renderedImage.createGraphics();
        layoutChildren();
        for (LayerObject layer: getChildren()) {
            layer.render(g2d, aoi, ignoreRough);
        }
        g2d.dispose();

        BufferedImage mask = new BufferedImage(renderedImage.getWidth(), renderedImage.getHeight(), renderedImage.getType());
        g2d = mask.createGraphics();
        g2d.setPaint(Color.BLACK);
        g2d.fill(aoi);
        g2d.dispose();

        Composite composite = AlphaComposite.DstIn;
        var context = composite.createContext(mask.getColorModel(), renderedImage.getColorModel(), null);
        context.compose(mask.getData(), renderedImage.getRaster(), renderedImage.getRaster());
        context.dispose();

        g.drawImage(renderedImage, aoiRect.x, aoiRect.y, null);
    }

    @Override
    public Memento<?> createMemento() {
        return new MyMemento(this, super.createMemento(), new ArrayList<>(children));
    }

    @Override
    public void restore(Memento<?> memento) {
        if (memento == null) {
            throw new NullPointerException("memento is null!");
        }

        super.restore(memento.getParent());

        if (memento instanceof MyMemento m) {
            children.clear();
            children.addAll(m.children);
        } else {
            throw new IncompatibleMementoException("\"memento\" is not compatible to LayerObject.");
        }
    }

    private record MyMemento(ParentBase parent, Memento<?> superMemento, List<LayerObject> children) implements Memento<ParentBase> {
        @Override
        public ParentBase getOriginator() {
            return parent;
        }

        @Override
        public Memento<?> getParent() {
            return superMemento;
        }
    }

    private static class ParentLayerList extends AbstractList<LayerObject> implements LayerList, RandomAccess {
        private final ParentBase parent;
        private final ArrayList<LayerObject> backingList;

        public ParentLayerList(ParentBase parent, Collection<LayerObject> c) {
            this.parent = parent;
            backingList = new ArrayList<>(c);
        }

        public ParentLayerList(ParentBase parent) {
            this.parent = parent;
            backingList = new ArrayList<>();
        }

        @Override
        public void add(int i, LayerObject e) {
            Objects.requireNonNull(e, "layer == null");
            backingList.add(i, e);
            e.setParent(parent);
        }

        @Override
        public LayerObject remove(int index) {
            var res = backingList.remove(index);
            res.setParent(null);
            return res;
        }

        @Override
        public int size() {
            return backingList.size();
        }

        @Override
        public LayerObject get(int index) {
            return backingList.get(index);
        }
    }
}
