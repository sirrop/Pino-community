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

package jp.gr.java_conf.alpius.pino.ui.skin;

import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;

import static jp.gr.java_conf.alpius.pino.internal.InternalLogger.*;

/**
 * スキンが未実装である場合に使用されます
 */
public class NullSkin extends SkinBase<Control> {
    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected NullSkin(Control control) {
        super(control);
    }

    public static NullSkin of(Control control) {
        log("NullSkin is created for %s", control.getClass().getName());
        return new NullSkin(control);
    }
}
