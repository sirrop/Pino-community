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


import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerList;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;

import java.util.*;

public class PinoProjectLayerList extends ModifiableObservableListBase<LayerObject> implements LayerList, ObservableList<LayerObject>, RandomAccess {
    private final ObservableList<LayerObject> backingList;
    private final List<InvalidationListener> invalidationListeners = new ArrayList<>();
    private final List<ListChangeListener<? super LayerObject>> changeListeners = new ArrayList<>();

    public PinoProjectLayerList(Collection<? extends LayerObject> c) {
        backingList = FXCollections.observableArrayList(c);
    }

    public PinoProjectLayerList() {
        backingList = FXCollections.observableArrayList();
    }

    public LayerObject get(int index) {
        return backingList.get(index);
    }

    public int size() {
        return backingList.size();
    }

    @Override
    protected void doAdd(int index, LayerObject element) {
        backingList.add(index, element);
    }

    @Override
    protected LayerObject doSet(int index, LayerObject element) {
        return backingList.set(index, element);
    }

    @Override
    protected LayerObject doRemove(int index) {
        return backingList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return backingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }


    @Override
    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    @Override
    public void clear() {
        if (hasListeners()) {
            beginChange();
            nextRemove(0, this);
        }
        backingList.clear();
        ++modCount;
        if (hasListeners()) {
            endChange();
        }
    }

    @Override
    public void remove(int fromIndex, int toIndex) {
        beginChange();
        for (int i = fromIndex; i < toIndex; ++i) {
            remove(fromIndex);
        }
        endChange();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        beginChange();
        BitSet bs = new BitSet(c.size());
        for (int i = 0; i < size(); ++i) {
            if (c.contains(get(i))) {
                bs.set(i);
            }
        }
        if (!bs.isEmpty()) {
            int cur = size();
            while ((cur = bs.previousSetBit(cur - 1)) >= 0) {
                remove(cur);
            }
        }
        endChange();
        return !bs.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        beginChange();
        BitSet bs = new BitSet(c.size());
        for (int i = 0; i < size(); ++i) {
            if (!c.contains(get(i))) {
                bs.set(i);
            }
        }
        if (!bs.isEmpty()) {
            int cur = size();
            while ((cur = bs.previousSetBit(cur - 1)) >= 0) {
                remove(cur);
            }
        }
        endChange();
        return !bs.isEmpty();
    }
}
