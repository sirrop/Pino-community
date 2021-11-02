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

package jp.gr.java_conf.alpius.pino.graphics.internal.graphics.blend;

public class DebugStrategy implements SimpleBlender.BlendStrategy {
    private final SimpleBlender.BlendStrategy strategy;
    private final int w;
    private final int h;

    public DebugStrategy(int w, int h, SimpleBlender.BlendStrategy strategy) {
        this.w = w;
        this.h = h;
        this.strategy = strategy;
        System.out.printf("Width: %d, Height: %d\n", w, h);
    }

    private int count = 0;

    @Override
    public int calc(int src, int dst) {
        var x = count / h;
        var y = (count - x) / w;
        var res = strategy.calc(src, dst);
        System.out.printf("(%d, %d)=[a: %d, r: %d, g: %d, b: %d]\n", x, y, shr(res, 24), shr(res, 16), shr(res, 8), shr(res, 0));
        count += 1;
        return res;
    }

    private static int shr(int value, int bit) {
        return (value >> bit & 0xff);
    }
}
