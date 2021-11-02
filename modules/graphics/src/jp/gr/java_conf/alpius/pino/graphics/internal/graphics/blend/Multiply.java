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

public class Multiply implements SimpleBlender.BlendStrategy {
    private interface SingletonHolder {
        Multiply instance = new Multiply();
    }

    public static Multiply getInstance() {
        return SingletonHolder.instance;
    }

    private Multiply() {
    }

    @Override
    public int calc(int src, int dst) {
        float srcAlpha = src >> 24 & 0xff;
        float srcRed = src >> 16 & 0xff;
        float srcGreen = src >> 8 & 0xff;
        float srcBlue = src & 0xff;

        float dstAlpha = dst >> 24 & 0xff;
        float dstRed = dst >> 16 & 0xff;
        float dstGreen = dst >> 8 & 0xff;
        float dstBlue = dst & 0xff;

        float a1 = srcAlpha * dstAlpha / 255;
        float a2 = srcAlpha * (255 - dstAlpha) / 255;
        float a3 = (255 - srcAlpha) * dstAlpha / 255;
        float a = a1 + a2 + a3;

        if (a == 0) {
            return 0;
        }

        int red = Math.round((a1 * srcRed * dstRed / 255 + a2 * srcRed + a3 * dstRed) / a);
        int green = Math.round((a1 * srcGreen * dstGreen / 255 + a2 * srcGreen + a3 * dstGreen) / a);
        int blue = Math.round((a1 * srcBlue * dstBlue / 255 + a2 * srcBlue + a3 * dstBlue) / a);

        a /= 255;

        return Math.round(a) << 24 | red << 16 | green << 8 | blue;
    }
}
