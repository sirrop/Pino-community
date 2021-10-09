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

package jp.gr.java_conf.alpius.pino.graphics;

import jp.gr.java_conf.alpius.pino.graphics.internal.graphics.SimpleBlender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleBlenderTest {
    @Test
    public void doTest() {
        var src = joinArgb(0xff, 100, 100, 100);
        var dst = joinArgb(0xff, 155, 155, 155);

        var plus = SimpleBlender.getStrategy(AlphaBlend.getInstance(AlphaBlend.Mode.PLUS));

        assertEquals(joinArgb(0xff, 0xff, 0xff, 0xff), plus.calc(src, dst));
    }

    private int joinArgb(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }
}
