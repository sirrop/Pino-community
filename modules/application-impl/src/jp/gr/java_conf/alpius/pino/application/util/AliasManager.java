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

package jp.gr.java_conf.alpius.pino.application.util;

import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;
import jp.gr.java_conf.alpius.pino.tool.plugin.HandTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AliasManager implements IAliasManager{
    public static AliasManager create() {
        var res = new AliasManager();
        res.register(DrawTool.class.getName()).as("描画ツール");
        res.register(HandTool.class.getName()).as("移動");
        return res;
    }

    private final Map<String, String> aliasMap = new HashMap<>();

    public AliasRegisterApi register(String original) {
        return alias -> aliasMap.put(original, alias);
    }

    public Optional<String> getAlias(String original) {
        return Optional.of(aliasMap.get(original));
    }

    public Optional<String> getOriginal(String alias) {
        for (var key: aliasMap.keySet()) {
            var res = aliasMap.get(key);
            if (Objects.equals(res, alias)) {
                return Optional.of(key);
            }
        }
        return Optional.empty();
    }
}
