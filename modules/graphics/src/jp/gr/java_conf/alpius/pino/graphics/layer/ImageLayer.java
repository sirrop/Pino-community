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

import jp.gr.java_conf.alpius.pino.memento.IncompatibleMementoException;
import jp.gr.java_conf.alpius.pino.memento.Memento;
import jp.gr.java_conf.alpius.pino.memento.MementoBase;

import java.awt.*;
import java.util.Objects;

public final class ImageLayer extends LayerObject {
    private Image image;
    private boolean keepReference = true;

    public ImageLayer(Image image) {
        this.image = image;
    }

    public ImageLayer() {
        this(null);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    /**
     * {@link ImageLayer#restore(Memento)}の際にimageへの参照を保持するか、新しいオブジェクトを使用するか指定します。
     * @param keep trueの場合は参照を保持する
     * @see ImageLayer#isKeepReference()
     */
    public void setKeepReference(boolean keep) {
        keepReference = keep;
    }

    /**
     * @return trueの場合{@link ImageLayer#restore(Memento)}の際にimageへの参照を保持し、falseの場合新しいオブジェクトを使用します
     * @see ImageLayer#setKeepReference(boolean)
     */
    public boolean isKeepReference() {
        return keepReference;
    }

    @Override
    void renderContent(Graphics2D g, Shape aoi, boolean ignoreRough) {
        if (image == null) {
            return;
        }
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void dispose() {
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    @Override
    public Memento<?> createMemento() {
        return new MyMemento(this, super.createMemento());
    }

    /**
     * Mementoから状態を復元します。
     * <p>
     *     {@link ImageLayer#isKeepReference()}でtrueが返され、かつimageがnullでなく{@link Image#getGraphics()}で返すオブジェクトが{@link Graphics2D}のサブクラスでない場合、IllegalStateExceptionが投げられます
     * </p>
     * @param memento このオブジェクトの状態を保存するMemento
     * @throws IllegalStateException 上記の通り
     */
    @Override
    public void restore(Memento<?> memento) {
        super.restore(Objects.requireNonNull(memento).getParent());
        if (memento instanceof MyMemento m) {
            if (keepReference) {
                try {
                    var g = (Graphics2D) image.getGraphics();
                    g.setComposite(AlphaComposite.Src);
                    g.drawImage(m.image, 0, 0, null);
                    g.dispose();
                } catch (UnsupportedOperationException | ClassCastException e) {
                    throw new IllegalStateException("Can't overwrite image.", e);
                }
            } else {
                image = m.image;
            }
        } else {
            throw new IncompatibleMementoException("\"memento\" is not compatible.");
        }
    }

    private static final class MyMemento extends MementoBase<ImageLayer> {
        private final Image image;

        public MyMemento(ImageLayer layer, Memento<?> parent) {
            super(layer, parent);
            image = Toolkit.getDefaultToolkit().createImage(layer.image.getSource());
        }
    }
}
