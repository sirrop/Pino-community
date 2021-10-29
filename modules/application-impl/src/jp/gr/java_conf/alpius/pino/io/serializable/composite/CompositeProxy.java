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

package jp.gr.java_conf.alpius.pino.io.serializable.composite;

import jp.gr.java_conf.alpius.pino.annotation.Beta;
import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;

import java.io.Serial;
import java.io.Serializable;

@Beta
public abstract class CompositeProxy implements Serializable {
    public static CompositeProxy create(CompositeFactory composite) {
        if (composite instanceof AlphaBlend.Mode mode) {
            return new BlendModeProxy(mode);
        }
        throw new IllegalArgumentException("Unknown composite factory");
    }

    @Serial
    private static final long serialVersionUID = 7760603310662796329L;


    public abstract CompositeFactory create();
}
