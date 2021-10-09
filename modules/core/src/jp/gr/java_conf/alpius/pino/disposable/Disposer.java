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

package jp.gr.java_conf.alpius.pino.disposable;

public final class Disposer {
    private Disposer() {}

    public static Disposable newDisposable() {
        return newDisposable("newDisposable");
    }

    public static Disposable newDisposable(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("name is null, empty or blank.");
        }
        return new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    private static final ObjectTree tree = new ObjectTree();

    public static void registerDisposable(Disposable parent, Disposable child) {
        tree.register(parent, child);
    }

    public static boolean isDisposed(Disposable disposable) {
        return tree.isDisposed(disposable);
    }

    public static void dispose(Disposable disposable) {
        tree.executeAll(disposable);
    }
}
