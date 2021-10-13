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

import jp.gr.java_conf.alpius.pino.beans.BeanPeer;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.util.Strings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBrush implements Brush {
    private BeanPeer<Brush> beanPeer;

    private BeanPeer<Brush> getPeer() {
        if (beanPeer == null) {
            beanPeer = new BeanPeer<>(this);
        }
        return beanPeer;
    }

    public void addListener(PropertyChangeListener listener) {
        getPeer().addListener(listener);
    }

    public void addListener(PropertyChangeListener listener, Disposable parent) {
        getPeer().addListener(listener, parent);
    }

    public void removeListener(PropertyChangeListener listener) {
        getPeer().removeListener(listener);
    }


    @Override
    public List<PropertyDescriptor> getUnmodifiablePropertyList() {
        return getPeer().getUnmodifiableProperties();
    }

    protected <T> void firePropertyChange(String propertyName, T oldValue, T newValue) {
        getPeer().firePropertyChange(propertyName, oldValue, newValue);
    }

    private String name = getClass().getSimpleName();

    public void setName(String value) {
        if (!Objects.equals(this.name, value)) {
            String old = name;
            if (Strings.isNullOrEmpty(value)) {
                name = getClass().getSimpleName();
            } else {
                name = value;
            }
            firePropertyChange("name", old, name);
        }
    }

    public String getName() {
        return name;
    }
}
