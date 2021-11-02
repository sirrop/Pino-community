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

public class SoftLight implements SimpleBlender.BlendStrategy {
    private interface SingletonHolder {
        SoftLight instance = new SoftLight();
    }

    public static SoftLight getInstance() {
        return SingletonHolder.instance;
    }

    private SoftLight() {
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

        int dodgeRed;
        if (srcRed < 128) {
            dodgeRed = dstRed - (255 - 2 * srcRed) * dstRed * (255 - dstRed) / (255 * 255);
        } else {
            if (dstRed < 64) {
                dodgeRed = dstRed + (2 * srcRed - 255) * (((16 * dstRed - 12 * 255) * dstRed + 4 * 255) - dstRed);
            } else {
                dodgeRed = dstRed + (2 * srcRed - 255) * ((int) Math.sqrt(dstRed) - dstRed);
            }
        }

        int dodgeGreen;
        if (srcGreen < 128) {
            dodgeGreen = dstGreen - (255 - 2 * srcGreen) * dstGreen * (255 - dstGreen) / (255 * 255);
        } else {
            if (dstGreen < 64) {
                dodgeGreen = dstGreen + (2 * srcGreen - 255) * (((16 * dstGreen - 12 * 255) * dstGreen + 4 * 255) - dstGreen);
            } else {
                dodgeGreen = dstGreen + (2 * srcGreen - 255) * ((int) Math.sqrt(dstGreen) - dstGreen);
            }
        }

        int dodgeBlue;
        if (srcBlue < 128) {
            dodgeBlue = dstBlue - (255 - 2 * srcBlue) * dstBlue * (255 - dstBlue) / (255 * 255);
        } else {
            if (dstBlue < 64) {
                dodgeBlue = dstBlue + (2 * srcBlue - 255) * (((16 * dstBlue - 12 * 255) * dstBlue + 4 * 255) - dstBlue);
            } else {
                dodgeBlue = dstBlue + (2 * srcBlue - 255) * ((int) Math.sqrt(dstBlue) - dstBlue);
            }
        }

        int red = (a1 * dodgeRed + a2 * srcRed + a3 * dstRed) / a;
        int green = (a1 * dodgeGreen + a2 * srcGreen + a3 * dstGreen) / a;
        int blue = (a1 * dodgeBlue + a2 * srcBlue + a3 * dstBlue) / a;

        a /= 255;

        return a << 24 | red << 16 | green << 8 | blue;
    }
}
