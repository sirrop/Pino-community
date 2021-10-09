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

package jp.gr.java_conf.alpius.pino.beans;

import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class PinoResourceBundle extends ResourceBundle {
    private final ResourceBundle thisBundle;
    private final ResourceBundle parent;

    private PinoResourceBundle(ResourceBundle thisBundle, ResourceBundle parent) {
        this.thisBundle = thisBundle;
        this.parent = parent;
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        try {
            return thisBundle.getObject(key);
        } catch (MissingResourceException ignored) {
            if (parent == null) return null;
            return parent.getObject(key);
        }
    }

    @Override
    public @NotNull Enumeration<String> getKeys() {
        return new Enumeration<>() {
            final Enumeration<String> thisEnumeration = thisBundle.getKeys();
            final Enumeration<String> parentEnumeration = parent == null ? null : parent.getKeys();

            @Override
            public boolean hasMoreElements() {
                return !thisEnumeration.hasMoreElements() && parentEnumeration != null && parentEnumeration.hasMoreElements();
            }

            @Override
            public String nextElement() {
                if (thisEnumeration.hasMoreElements() || parentEnumeration == null) {
                    return thisEnumeration.nextElement();
                } else {
                    return parentEnumeration.nextElement();
                }
            }
        };
    }

    public static PinoResourceBundle of(ResourceBundle thisBundle, ResourceBundle parent) {
        if (thisBundle == null) return null;
        return new PinoResourceBundle(thisBundle, parent);
    }
}
