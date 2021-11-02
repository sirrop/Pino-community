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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * forを使用して計算する実装です
 */
public class SimpleAlphaBlendContext implements CompositeContext {
    private final AlphaBlend blend;
    private final ColorModel srcCM;
    private final ColorModel dstCM;

    public SimpleAlphaBlendContext(AlphaBlend blend, ColorModel srcCM, ColorModel dstCM) {
        this.blend = blend;
        this.srcCM = srcCM;
        this.dstCM = dstCM;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        WritableRaster srcRas;
        if (src instanceof WritableRaster) {
            srcRas = (WritableRaster) src;
        } else {
            srcRas = src.createCompatibleWritableRaster();
            srcRas.setDataElements(0, 0, src);
        }
        if (dstIn != dstOut) {
            dstOut.setDataElements(0, 0, dstIn);
        }

        BufferedImage srcSurface = new BufferedImage(srcCM, srcRas, srcCM.isAlphaPremultiplied(), null);
        BufferedImage dstSurface = new BufferedImage(dstCM, dstOut, dstCM.isAlphaPremultiplied(), null);

        SimpleBlender.blend(srcSurface, dstSurface, blend);
    }
}
