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

package jp.gr.java_conf.alpius.pino.gui.widget.behavior;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.gui.widget.LayerCell;
import jp.gr.java_conf.alpius.pino.project.Project;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LayerCellBehavior extends BehaviorBase<LayerCell> {
    private static final DataFormat DF_INDEX = new DataFormat("application/index");

    public LayerCellBehavior(LayerCell cell) {
        super(cell);
        addEventHandler(MouseEvent.DRAG_DETECTED, this::dragDetected);
        addEventHandler(DragEvent.DRAG_OVER, this::dragOver);
        addEventHandler(DragEvent.DRAG_EXITED, this::dragExited);
        addEventHandler(DragEvent.DRAG_DROPPED, this::dragDropped);
    }

    private Image getImage(int w, int h, int strokeWidth) {
        LayerObject selectedLayer = getNode().getItem();
        Project p = Pino.getApp().getProject();
        BufferedImage layerImage = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        var g = layerImage.createGraphics();
        selectedLayer.render(g, new Rectangle(p.getWidth(), p.getHeight()), false);
        g.dispose();
        var dimension = computeDimension(layerImage.getWidth(), layerImage.getHeight(), w, h);
        BufferedImage scaledImage = new BufferedImage(dimension.width + strokeWidth * 2, dimension.height + strokeWidth * 2, BufferedImage.TYPE_INT_ARGB);
        g = scaledImage.createGraphics();
        g.setPaint(Color.GRAY);
        g.fillRect(0, 0, w + strokeWidth * 2, h + strokeWidth * 2);
        g.setComposite(AlphaComposite.Src);
        g.drawImage(layerImage.getScaledInstance(dimension.width, dimension.height, java.awt.Image.SCALE_SMOOTH), strokeWidth, strokeWidth, null);
        g.dispose();
        return SwingFXUtils.toFXImage(scaledImage, null);
    }

    private static Dimension computeDimension(int originalWidth, int originalHeight, int preferredWidth, int preferredHeight) {
        double preferredRatio = ((double) preferredWidth) / preferredHeight;
        double originalRatio = ((double) originalWidth) / originalHeight;
        int w, h;
        if (originalRatio > preferredRatio) {
            w = preferredWidth;
            h = (int) Math.round(w / originalRatio);
        } else {
            h = preferredHeight;
            w = (int) Math.round(h * originalRatio);
        }
        return new Dimension(w, h);
    }

    private void dragDetected(MouseEvent e) {
        int index = getNode().getIndex();
        Image image = getImage(50, 50, 1);
        Dragboard dragboard = getNode().getListView().startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.put(DF_INDEX, index);
        dragboard.setDragView(image);
        dragboard.setContent(content);
        e.consume();
    }

    private void dragOver(DragEvent e) {
        if (e.getGestureSource() == getNode().getListView()) {
            e.acceptTransferModes(TransferMode.MOVE);
            var node = getNode();
            var bounds = node.getBoundsInLocal();
            var y = e.getY();
            getNode().hideLabel();
            if (y >= bounds.getCenterY()) {
                getNode().showLowerLabel();
            } else {
                getNode().showUpperLabel();
            }
        }
        e.consume();
    }

    private void dragExited(DragEvent e) {
        getNode().hideLabel();
        e.consume();
    }

    private void dragDropped(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        if (dragboard.getContent(DF_INDEX) != null) {
            var srcIdx = (int) dragboard.getContent(DF_INDEX);
            var thisItem = getNode().getItem();
            var list = getNode().getListView();
            var items = list.getItems();
            var src = items.remove(srcIdx);
            var thisIndex = items.indexOf(thisItem);
            if (e.getY() >= getNode().getBoundsInLocal().getCenterY()) {
                items.add(coerce(thisIndex + 1).in(0, items.size()), src);
            } else {
                items.add(coerce(thisIndex).in(0, items.size()), src);
            }
            e.setDropCompleted(true);
            getNode().hideLabel();
        } else {
            e.setDropCompleted(false);
        }
        getNode().getListView().refresh();
        e.consume();
    }

    private static Coerce coerce(int value) {
        return new Coerce(value);
    }

    private static class Coerce {
        private final int value;

        public Coerce(int value) {
            this.value = value;
        }

        public int in(int min, int max) {
            if (value < min) {
                return min;
            } else {
                return Math.min(value, max);
            }
        }
    }
}
