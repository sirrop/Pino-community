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

package jp.gr.java_conf.alpius.pino.graphics.canvas;

import jp.gr.java_conf.alpius.pino.annotation.Beta;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Canvas extends Disposable {

    // used default canvas background
    Color TRANSPARENT = new Color(0, true);

    static Canvas createGeneral() {
        return new GeneralCanvas();
    }

    @Beta
    static Canvas createAccelerated() {
        System.err.println("Accelerated canvas is unstable.");
        return new AcceleratedCanvas();
    }

    void setBackground(Paint paint);
    Paint getBackground();
    void setSize(int w, int h);
    default void setSize(Dimension size) {
        setSize(size.width, size.height);
    }
    default Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }
    int getWidth();
    int getHeight();
    Graphics2D createGraphics();
    default Image createCompatibleImage(int transparency) {
        return createCompatibleImage(getWidth(), getHeight(), transparency, getBackground());
    }
    Image createCompatibleImage(int w, int h, int transparency, Paint background);
    BufferedImage snapshot();
}
