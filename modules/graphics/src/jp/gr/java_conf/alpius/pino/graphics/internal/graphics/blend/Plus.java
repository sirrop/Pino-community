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

public class Plus implements SimpleBlender.BlendStrategy {
    private interface SingletonHolder {
        Plus instance = new Plus();
    }

    public static Plus getInstance() {
        return SingletonHolder.instance;
    }

    private Plus() {
    }

    @Override
    public int calc(int src, int dst) {
        int srcAlpha = src >> 24 & 0xff;
        int srcRed = src >> 16 & 0xff;
        int srcGreen = src >> 8 & 0xff;
        int srcBlue = src & 0xff;

        int dstAlpha = dst >> 24 & 0xff;
        int dstRed = dst >> 16 & 0xff;
        int dstGreen = dst >> 8 & 0xff;
        int dstBlue = dst & 0xff;

        if (srcAlpha == 255 && dstAlpha == 255) {
            return 255 << 24 |
                    Math.min(srcRed + dstRed, 255) << 16 |
                    Math.min(srcGreen + dstGreen, 255) << 8 |
                    Math.min(srcBlue + dstBlue, 255);
        } else {
            int a1 = srcAlpha * dstAlpha;
            int a2 = srcAlpha * (255 - dstAlpha);
            int a3 = (255 - srcAlpha) * dstAlpha;
            int a = a1 + a2 + a3;

            if (a == 0) {
                return 0;
            }

            int red = (a1 * Math.min(srcRed + dstRed, 255) + a2 * srcRed + a3 * dstRed) / a;
            int green = (a1 * Math.min(srcGreen + dstGreen, 255) + a2 * srcGreen + a3 * dstGreen) / a;
            int blue = (a1 * Math.min(srcBlue + dstBlue, 255) + a2 * srcBlue + a3 * dstBlue) / a;

            a /= 255;

            return a << 24 | red << 16 | green << 8 | blue;
        }
    }
}
