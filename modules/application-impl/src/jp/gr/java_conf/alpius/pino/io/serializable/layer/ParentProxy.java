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

package jp.gr.java_conf.alpius.pino.io.serializable.layer;

import jp.gr.java_conf.alpius.pino.graphics.layer.Parent;

import java.io.Serial;

public abstract class ParentProxy extends LayerProxy {
    @Serial
    private static final long serialVersionUID = -1794086762675317674L;

    protected ParentProxy(Parent layer) {
        super(layer);
    }

    @Override
    public abstract Parent createLayer(int w, int h);
}