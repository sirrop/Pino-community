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

package jp.gr.java_conf.alpius.pino.io.serializable.canvas;

import jp.gr.java_conf.alpius.pino.graphics.canvas.Canvas;
import jp.gr.java_conf.alpius.pino.graphics.canvas.internal.GeneralCanvas;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GeneralCanvasProxy extends CanvasProxy {
    @Serial
    private static final long serialVersionUID = -3107903084260557283L;

    private static final int TYPE_INT_ARGB = 0;
    private static final int TYPE_BYTE_GRAY = 1;

    private transient int type;
    private transient byte[] data;

    public GeneralCanvasProxy(GeneralCanvas canvas) {
        var surface = canvas.getSurface();
        type = getType(surface);
        data = getData(type, surface);
    }

    private static int getType(BufferedImage surface) {
        if (surface.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return TYPE_BYTE_GRAY;
        }
        return TYPE_INT_ARGB;
    }

    private byte[] getData(int type, BufferedImage surface) {
        ByteBuffer buffer;
        if (type == TYPE_BYTE_GRAY) {
            buffer = readByteGray(surface);
        } else {
            buffer = readIntArgb(surface);
        }
        return buffer.array();
    }

    private static ByteBuffer readByteGray(BufferedImage surface) {
        int w = surface.getWidth();
        int h = surface.getHeight();
        ByteBuffer buffer = ByteBuffer.allocate(w * h);
        var data = (DataBufferByte) surface.getData().getDataBuffer();
        buffer.put(data.getData()).rewind();
        return buffer;
    }

    private static ByteBuffer readIntArgb(BufferedImage surface) {
        int w = surface.getWidth();
        int h = surface.getHeight();
        ByteBuffer buffer = ByteBuffer.allocate(w * h * 4);
        var data = surface.getRGB(0, 0, w, h, null, 0, w);
        for (int pixel: data) {
            buffer.putInt(pixel);
        }
        buffer.rewind();
        return buffer;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(type);
        out.writeInt(data.length);
        out.write(data);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int type = in.readInt();
        int len = in.readInt();
        byte[] data = in.readNBytes(len);
        if (type != TYPE_INT_ARGB && type != TYPE_BYTE_GRAY) {
            throw new InvalidObjectException("unknown type");
        }
        if (data == null) {
            throw new InvalidObjectException("data is null");
        }
        this.type = type;
        this.data = data;
    }

    @Override
    public Canvas createCanvas(int w, int h) {
        GeneralCanvas canvas = new GeneralCanvas();
        canvas.setSize(w, h);
        BufferedImage surface = restoreSurface(w, h);
        var g = canvas.createGraphics();
        g.drawImage(surface, 0, 0, null);
        g.dispose();
        return canvas;
    }

    private BufferedImage restoreSurface(int w, int h) {
        if (type == TYPE_BYTE_GRAY) {
            return restoreByteGraySurface(w, h);
        } else {
            return restoreIntArgbSurface(w, h);
        }
    }

    private BufferedImage restoreByteGraySurface(int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        DataBufferByte buffer = (DataBufferByte) image.getData().getDataBuffer();

        int offset = 0;
        for (byte pixel: data) {
            buffer.setElem(offset, pixel);
            ++offset;
        }

        return image;
    }

    private BufferedImage restoreIntArgbSurface(int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        IntBuffer buffer = ByteBuffer.wrap(data).asIntBuffer();
        int[] data = new int[w * h];

        while (buffer.position() < buffer.capacity()) {
            int index = buffer.position();
            int value = buffer.get();
            data[index] = value;
        }

        image.setRGB(0, 0, w, h, data, 0, w);
        return image;
    }
}
