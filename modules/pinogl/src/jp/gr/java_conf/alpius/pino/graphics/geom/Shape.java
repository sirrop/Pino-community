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

package jp.gr.java_conf.alpius.pino.graphics.geom;

import jp.gr.java_conf.alpius.pino.graphics.transform.Affine2D;
import jp.gr.java_conf.alpius.pino.graphics.transform.Transform;

import java.util.Iterator;

public abstract class Shape implements Iterable<PathElement> {
    Shape() {
    }

    public abstract Iterator<PathElement> iterator(Transform tx);

    @Override
    public Iterator<PathElement> iterator() {
        return iterator(Affine2D.makeIdentity());
    }
}
