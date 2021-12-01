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

package jp.gr.java_conf.alpius.pino.graphics.paint;

import jp.gr.java_conf.alpius.pino.graphics.Composite;
import jp.gr.java_conf.alpius.pino.graphics.Image;

public class ImagePaintBuilder extends PaintBuilder {
    public static final int USE_IMAGE_SIZE = 0;

    private Image image;
    private int x = 0;
    private int y = 0;
    private int w = USE_IMAGE_SIZE;
    private int h = USE_IMAGE_SIZE;


    @Override
    public ImagePaintBuilder setAntialias(boolean antialias) {
        return (ImagePaintBuilder) super.setAntialias(antialias);
    }

    @Override
    public ImagePaintBuilder setComposite(Composite composite) {
        return (ImagePaintBuilder) super.setComposite(composite);
    }

    @Override
    public ImagePaintBuilder setOpacity(float opacity) {
        return (ImagePaintBuilder) super.setOpacity(opacity);
    }

    public ImagePaintBuilder setImage(Image image) {
        this.image = image;
        return this;
    }

    public Image getImage() {
        return image;
    }

    public ImagePaintBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public int getX() {
        return x;
    }

    public ImagePaintBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public int getY() {
        return y;
    }

    public ImagePaintBuilder setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ImagePaintBuilder setWidth(int width) {
        this.w = width;
        return this;
    }

    public int getWidth() {
        return w;
    }

    public ImagePaintBuilder setHeight(int height) {
        this.h = height;
        return this;
    }

    public int getHeight() {
        return h;
    }

    public ImagePaintBuilder setSize(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }

    public ImagePaint build() {
        int w, h;
        if (this.w <= USE_IMAGE_SIZE) {
            w = getImage().getWidth();
        } else {
            w = this.w;
        }
        if (this.h <= USE_IMAGE_SIZE) {
            h = getImage().getHeight();
        } else {
            h = this.h;
        }
        return new ImagePaint(isAntialias(), getComposite(), getOpacity(), getImage(), x, y, w, h);
    }
}
