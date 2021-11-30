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

import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;
import jp.gr.java_conf.alpius.pino.tool.plugin.EyedropperTool;
import jp.gr.java_conf.alpius.pino.tool.plugin.HandTool;
import jp.gr.java_conf.alpius.pino.tool.plugin.Ruler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AliasManager implements IAliasManager{
    public static AliasManager create() {
        var res = new AliasManager();

        /* Tools */
        res.register(DrawTool.class.getName()).as("描画ツール");
        res.register(EyedropperTool.class.getName()).as("スポイト");
        res.register(HandTool.class.getName()).as("移動");
        res.register(Ruler.class.getName()).as("定規");

        /* CompositeFactories */
        res.register(AlphaBlend.Mode.SRC_OVER.name()).as("通常");
        res.register(AlphaBlend.Mode.PLUS.name()).as("加算");
        res.register(AlphaBlend.Mode.MULTIPLY.name()).as("乗算");
        res.register(AlphaBlend.Mode.OVERLAY.name()).as("オーバーレイ");
        res.register(AlphaBlend.Mode.SCREEN.name()).as("スクリーン");
        res.register(AlphaBlend.Mode.DARKEN.name()).as("比較（暗）");
        res.register(AlphaBlend.Mode.LIGHTEN.name()).as("比較（明）");
        res.register(AlphaBlend.Mode.COLOR_DODGE.name()).as("覆い焼きカラー");
        res.register(AlphaBlend.Mode.COLOR_BURN.name()).as("覆い焼き込みカラー");
        res.register(AlphaBlend.Mode.HARD_LIGHT.name()).as("ハードライト");
        res.register(AlphaBlend.Mode.SOFT_LIGHT.name()).as("ソフトライト");
        res.register(AlphaBlend.Mode.DIFFERENCE.name()).as("差分");
        res.register(AlphaBlend.Mode.EXCLUSION.name()).as("除外");
        res.register(AlphaBlend.Mode.LINEAR_BURN.name()).as("焼き込みリニア");
        res.register(AlphaBlend.Mode.VIVID_LIGHT.name()).as("ビビッドライト");
        res.register(AlphaBlend.Mode.PIN_LIGHT.name()).as("ピンライト");
        res.register(AlphaBlend.Mode.DIVIDE.name()).as("除算");
        return res;
    }

    private final Map<String, String> aliasMap = new HashMap<>();

    public AliasRegisterApi register(String original) {
        return alias -> aliasMap.put(original, alias);
    }

    public Optional<String> getAlias(String original) {
        return Optional.ofNullable(aliasMap.get(original));
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
