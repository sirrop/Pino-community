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

package jp.gr.java_conf.alpius.pino.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SimpleServiceContainer implements MutableServiceContainer {
    private final Map<Class<?>, Supplier<?>> map = new HashMap<>();
    @Override
    public <T> void register(Class<T> base, Supplier<? extends T> supplier) {
        map.put(base, supplier);
    }
    @Override
    public <T> void register(Class<T> base, T instance) {
        map.put(base, () -> instance);
    }
    @Override
    public void unregister(Class<?> base) {
        map.remove(base);
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getService(Class<T> serviceClass) {
        var supplier = map.get(serviceClass);
        if (supplier == null) {
            return null;
        }
        return (T) map.get(serviceClass).get();
    }
}
