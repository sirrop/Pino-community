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

package jp.gr.java_conf.alpius.pino.graphics.internal.graphics;

import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.awt.image.BufferedImage;

public final class SimpleBlender {
    private SimpleBlender() {
    }

    public static void blend(BufferedImage src, BufferedImage dst, AlphaBlend blend) {
        int w = Math.min(src.getWidth(), dst.getWidth());
        int h = Math.min(src.getWidth(), dst.getHeight());
        int loop = w * h;
        int[] srcBuf = src.getRGB(0, 0, w, h, null, 0, w);
        int[] dstBuf = src.getRGB(0, 0, w, h, null, 0, w);
        int[] resBuf = new int[loop];

        BlendStrategy strategy = getStrategy(blend);

        for (int i = 0; i < loop; i++) {
            int srcColor = srcBuf[i];
            int dstColor = dstBuf[i];
            resBuf[i] = strategy.calc(srcColor, dstColor);
        }

        dst.setRGB(0, 0, w, h, resBuf, 0, w);
    }

    @VisibleForTesting
    public interface BlendStrategy {
        int calc(int src, int dst);
    }

    @NotNull
    @VisibleForTesting
    public static BlendStrategy getStrategy(AlphaBlend blend) {
        // TODO : test
        // テストが完了していません。
        // 計算結果が間違っている可能性があります。
        // 十分にテストを行い、計算結果が正しいという確信が得られたらこのコメントは削除してください。
        return switch (blend.getBlendMode()) {
            case PLUS -> (src, dst) -> {
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

                int red = (a1 * Math.min(srcRed + dstRed, 255) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * Math.min(srcGreen + dstGreen, 255) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * Math.min(srcBlue + dstBlue, 255) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case MULTIPLY -> (src, dst) -> {
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

                int red = (a1 * Math.min(srcRed * dstRed / 255, 255) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * Math.min(srcGreen * dstGreen / 255, 255) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * Math.min(srcBlue * dstBlue / 255, 255) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case SCREEN -> (src, dst) -> {
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

                int red = (a1 * (255 - (255 - srcRed) * (255 - dstRed) / 255) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * (255 - (255 - srcGreen) * (255 - dstGreen) / 255) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * (255 - (255 - srcBlue) * (255 - dstBlue) / 255) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case OVERLAY -> (src, dst) -> {
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
            };
            case DARKEN -> (src, dst) -> {
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

                int red = (a1 * Math.min(srcRed, dstRed) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * Math.min(srcGreen, dstGreen) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * Math.min(srcBlue, dstBlue) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case LIGHTEN -> (src, dst) -> {
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

                int red = (a1 * Math.max(srcRed, dstRed) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * Math.max(srcGreen, dstGreen) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * Math.max(srcBlue, dstBlue) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case COLOR_DODGE -> (src, dst) -> {
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
                if (srcRed == 255) {
                    dodgeRed = 255;
                } else {
                    dodgeRed = Math.min((dstRed / (255 - srcRed)) * 255, 255);
                }

                int dodgeGreen;
                if (srcGreen == 255) {
                    dodgeGreen = 255;
                } else {
                    dodgeGreen = Math.min((dstGreen / (255 - srcGreen)) * 255, 255);
                }

                int dodgeBlue;
                if (srcBlue == 255) {
                    dodgeBlue = 255;
                } else {
                    dodgeBlue = Math.min((dstBlue / (255 - srcBlue)) * 255, 255);
                }

                int red = (a1 * dodgeRed + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * dodgeGreen + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * dodgeBlue + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case COLOR_BURN -> (src, dst) -> {
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
                if (srcRed == 0) {
                    dodgeRed = 0;
                } else {
                    dodgeRed = Math.max(255 - ((255 - dstRed) / srcRed) * 255, 0);
                }

                int dodgeGreen;
                if (srcGreen == 0) {
                    dodgeGreen = 0;
                } else {
                    dodgeGreen = Math.max(255 - ((255 - dstGreen) / srcGreen) * 255, 0);
                }

                int dodgeBlue;
                if (srcBlue == 0) {
                    dodgeBlue = 0;
                } else {
                    dodgeBlue = Math.max(255 - ((255 - dstBlue) / srcBlue) * 255, 0);
                }

                int red = (a1 * dodgeRed + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * dodgeGreen + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * dodgeBlue + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case HARD_LIGHT -> (src, dst) -> {
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
                    dodgeRed = dstRed * srcRed * 2 / 255;
                } else {
                    dodgeRed = 2 * (srcRed + dstRed - srcRed * dstRed / 255) - 255;
                }

                int dodgeGreen;
                if (srcGreen < 128) {
                    dodgeGreen = dstGreen * srcGreen * 2 / 255;
                } else {
                    dodgeGreen = 2 * (srcGreen + dstGreen - srcGreen * dstGreen / 255) - 255;
                }

                int dodgeBlue;
                if (srcBlue < 128) {
                    dodgeBlue = dstBlue * srcBlue * 2 / 255;
                } else {
                    dodgeBlue = 2 * (srcBlue + dstBlue - srcBlue * dstBlue / 255) - 255;
                }

                int red = (a1 * dodgeRed + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * dodgeGreen + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * dodgeBlue + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case SOFT_LIGHT -> (src, dst) -> {
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
            };
            case DIFFERENCE -> (src, dst) -> {
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

                int red = (a1 * Math.abs(srcRed - dstRed) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * Math.abs(srcGreen - dstGreen) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * Math.abs(srcBlue - dstBlue) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            case EXCLUSION -> (src, dst) -> {
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

                int red = (a1 * (dstRed + srcRed - 2 * srcRed * dstRed / 255) + a2 * srcRed + a3 * dstRed) / a;
                int green = (a1 * (dstGreen + srcGreen - 2 * srcGreen * dstGreen / 255) + a2 * srcGreen + a3 * dstGreen) / a;
                int blue = (a1 * (dstBlue + srcBlue - 2 * srcBlue * dstBlue / 255) + a2 * srcBlue + a3 * dstBlue) / a;

                a /= 255;

                return a << 24 | red << 16 | green << 8 | blue;
            };
            default -> throw new UnsupportedOperationException();
        };
    }
}
