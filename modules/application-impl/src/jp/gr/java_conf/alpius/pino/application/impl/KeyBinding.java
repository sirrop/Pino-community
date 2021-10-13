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

import javafx.scene.input.KeyCombination;
import javafx.util.Pair;

import java.util.List;

final class KeyBinding {

    public static List<Pair<KeyCombination, String>> getKeyConfigs() {
        return List.of(
                keyBinding("Shortcut+Z", "jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin.Undo"),
                keyBinding("Shortcut+Y", "jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin.Redo")
        );
    }

    private static Pair<KeyCombination, String> keyBinding(String key, String action) {
        return new Pair<>(KeyCombination.keyCombination(key), action);
    }
}
