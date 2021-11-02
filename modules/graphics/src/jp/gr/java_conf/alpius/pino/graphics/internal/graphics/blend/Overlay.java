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

public class Overlay implements SimpleBlender.BlendStrategy {
    private interface SingletonHolder {
        Overlay instance = new Overlay();
    }

    public static Overlay getInstance() {
        return SingletonHolder.instance;
    }

    private Overlay() {
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

        int a1 = srcAlpha * dstAlpha;
        int a2 = srcAlpha * (255 - dstAlpha);
        int a3 = (255 - srcAlpha) * dstAlpha;
        int a = a1 + a2 + a3;

        if (a == 0) {
            return 0;
        }

        int overlayRed;
        if (dstRed < 128) {
            overlayRed = srcRed * dstRed * 2 / 255;
        } else {
            overlayRed = 2 * (srcRed + dstRed - srcRed * dstRed / 255);
        }

        int overlayGreen;
        if (dstGreen < 128) {
            overlayGreen = srcGreen * dstGreen * 2 / 255;
        } else {
            overlayGreen = 2 * (srcGreen + dstGreen - srcGreen * dstGreen / 255);
        }

        int overlayBlue;
        if (dstBlue < 128) {
            overlayBlue = srcBlue * dstBlue * 2 / 255;
        } else {
            overlayBlue = 2 * (srcBlue + dstBlue - srcBlue * dstBlue / 255);
        }

        int red = (a1 * overlayRed + a2 * srcRed + a3 * dstRed) / a;
        int green = (a1 * overlayGreen + a2 * srcGreen + a3 * dstGreen) / a;
        int blue = (a1 * overlayBlue + a2 * srcBlue + a3 * dstBlue) / a;

        a /= 255;

        return a << 24 | red << 16 | green << 8 | blue;
    }
}
