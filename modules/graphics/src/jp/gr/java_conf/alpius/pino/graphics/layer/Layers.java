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

package jp.gr.java_conf.alpius.pino.graphics.layer;

import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public final class Layers {
    private Layers() {
    }

    public static <T extends LayerObject> T create(Class<T> clazz, int w, int h) {
        return create(getConstructor(clazz), w, h);
    }

    public static <T extends LayerObject> T createAccelerated(Class<T> clazz, int w, int h) {
        return createAccelerated(getConstructor(clazz), w, h);
    }

    public static <T extends LayerObject> T create(Supplier<T> constructor, int w, int h) {
        var layer = constructor.get();
        Canvas canvas = Canvas.createGeneral();
        canvas.setSize(w, h);
        layer.setCanvas(canvas);
        layer.init();
        return layer;
    }

    public static <T extends LayerObject> T createAccelerated(Supplier<T> constructor, int w, int h) {
        var layer = constructor.get();
        Canvas canvas = Canvas.createAccelerated();
        canvas.setSize(w, h);
        layer.setCanvas(canvas);
        layer.init();
        return layer;
    }

    public static BufferedImage snapshot(LayerObject layer) {
        return layer.getCanvas().snapshot();
    }

    private static <T> Supplier<T> getConstructor(Class<T> klass) {
        return () -> {
            try {
                return klass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
