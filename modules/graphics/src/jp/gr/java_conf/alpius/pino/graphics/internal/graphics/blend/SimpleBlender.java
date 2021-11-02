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

import jp.gr.java_conf.alpius.pino.graphics.AlphaBlend;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public final class SimpleBlender {
    private SimpleBlender() {
    }

    public static void blend(BufferedImage src, BufferedImage dst, AlphaBlend blend) {
        int w = Math.min(src.getWidth(), dst.getWidth());
        int h = Math.min(src.getWidth(), dst.getHeight());
        int loop = w * h;
        int[] srcBuf = src.getRGB(0, 0, w, h, null, 0, w);
        int[] dstBuf = dst.getRGB(0, 0, w, h, null, 0, w);
        int[] resBuf = new int[loop];

        BlendStrategy strategy = getStrategy(blend);

        for (int i = 0; i < loop; i++) {
            int srcColor = srcBuf[i];
            int dstColor = dstBuf[i];
            resBuf[i] = strategy.calc(srcColor, dstColor);
        }

        dst.setRGB(0, 0, w, h, resBuf, 0, w);
    }


    interface BlendStrategy {
        int calc(int src, int dst);
    }

    @NotNull
    private static BlendStrategy getStrategy(AlphaBlend blend) {
        // TODO : test
        // テストが完了していません。
        // 計算結果が間違っている可能性があります。
        // 十分にテストを行い、計算結果が正しいという確信が得られたらこのコメントは削除してください。
        return switch (blend.getBlendMode()) {
            case PLUS -> Plus.getInstance();
            case MULTIPLY -> Multiply.getInstance();
            case SCREEN -> Screen.getInstance();
            case OVERLAY -> Overlay.getInstance();
            case DARKEN -> Darken.getInstance();
            case LIGHTEN -> Lighten.getInstance();
            case COLOR_DODGE -> ColorDodge.getInstance();
            case COLOR_BURN -> ColorBurn.getInstance();
            case HARD_LIGHT -> HardLight.getInstance();
            case SOFT_LIGHT -> SoftLight.getInstance();
            case DIFFERENCE -> Difference.getInstance();
            case EXCLUSION -> Exclusion.getInstance();
            case LINEAR_BURN -> LinearBurn.getInstance();
            case VIVID_LIGHT -> VividLight.getInstance();
            case LINEAR_LIGHT -> LinearLight.getInstance();
            case PIN_LIGHT -> PinLight.getInstance();
            case DIVIDE -> Divide.getInstance();
            default -> throw new UnsupportedOperationException();
        };
    }
}
