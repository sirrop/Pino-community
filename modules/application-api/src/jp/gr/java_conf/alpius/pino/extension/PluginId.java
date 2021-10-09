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

package jp.gr.java_conf.alpius.pino.extension;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class PluginId implements Comparable<PluginId> {

    /* -- Static Field -- */

    private static final ConcurrentHashMap<String, PluginId> idMap = new ConcurrentHashMap<>();

    public static PluginId get(String id) {
        Objects.requireNonNull(id, "id == null!");
        return idMap.computeIfAbsent(id, PluginId::new);
    }

    public static Optional<PluginId> find(String id) {
        Objects.requireNonNull(id, "id == null!");
        return Optional.ofNullable(idMap.get(id));
    }

    public static Set<PluginId> getRegisteredIds() {
        return Set.copyOf(idMap.values());
    }


    /* -- Instance Field -- */

    private final String id;

    private PluginId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(@NotNull PluginId o) {
        Objects.requireNonNull(o);
        return id.compareTo(o.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o instanceof PluginId id) {
            return id.id.equals(this.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return id;
    }
}
