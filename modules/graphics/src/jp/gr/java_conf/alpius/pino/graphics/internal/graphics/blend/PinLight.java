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

public class PinLight implements SimpleBlender.BlendStrategy {
    private interface SingletonHolder {
        PinLight instance = new PinLight();
    }

    public static PinLight getInstance() {
        return SingletonHolder.instance;
    }

    private PinLight() {
    }

    @Override
    public int calc(int src, int dst) {
        final int srcAlpha = src >> 24 & 0xff;
        final int srcRed = src >> 16 & 0xff;
        final int srcGreen = src >> 8 & 0xff;
        final int srcBlue = src & 0xff;

        final int dstAlpha = dst >> 24 & 0xff;
        final int dstRed = dst >> 16 & 0xff;
        final int dstGreen = dst >> 8 & 0xff;
        final int dstBlue = dst & 0xff;

        final int red;
        if (srcRed < 128) {
            red = Math.min(dstRed, 2 * srcRed);
        } else {
            red = Math.max(dstRed, 2 * (srcRed - 128));
        }

        final int green;
        if (srcGreen < 128) {
            green = Math.min(dstGreen, 2 * srcRed);
        } else {
            green = Math.max(dstGreen, 2 * (srcGreen - 128));
        }

        final int blue;
        if (srcBlue < 128) {
            blue = Math.min(dstBlue, 2 * srcBlue);
        } else {
            blue = Math.max(dstBlue, 2 * (srcBlue - 128));
        }

        if (srcAlpha == 255 && dstAlpha == 255) {
            return 255 << 24 |
                    red << 16 |
                    green << 8 |
                    blue;
        } else {
            int a1 = srcAlpha * dstAlpha;
            int a2 = srcAlpha * (255 - dstAlpha);
            int a3 = (255 - srcAlpha) * dstAlpha;
            int a = a1 + a2 + a3;

            if (a == 0) {
                return 0;
            }

            int r = (a1 * red + a2 * srcRed + a3 * dstRed) / a;
            int g = (a1 * green + a2 * srcGreen + a3 * dstGreen) / a;
            int b = (a1 * blue + a2 * srcBlue + a3 * dstBlue) / a;

            a /= 255;

            return a << 24 | r << 16 | g << 8 | b;
        }
    }
}
