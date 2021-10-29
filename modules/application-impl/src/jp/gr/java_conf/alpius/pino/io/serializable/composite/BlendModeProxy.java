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

import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import jp.gr.java_conf.alpius.pino.graphics.CompositeFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Objects;

public class BlendModeProxy extends CompositeProxy {
    @Serial
    private static final long serialVersionUID = 7708728692125257995L;

    private transient String mode;

    public BlendModeProxy(AlphaBlend.Mode mode) {
        initialize(mode.name());
    }

    private void initialize(String mode) {
        Objects.requireNonNull(mode, "mode == null");
        for (var m: AlphaBlend.Mode.values()) {
            if (m.name().equals(mode)) {
                this.mode = mode;
                return;
            }
        }
        throw new IllegalArgumentException(mode + " is not found.");
    }

    @Override
    public CompositeFactory create() {
        return AlphaBlend.Mode.valueOf(mode);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(mode);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        var mode = (String) in.readObject();
        initialize(mode);
    }
}
