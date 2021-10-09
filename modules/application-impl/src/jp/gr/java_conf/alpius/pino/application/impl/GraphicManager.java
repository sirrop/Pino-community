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

package jp.gr.java_conf.alpius.pino.application.impl;

import javafx.scene.Node;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.brush.BrushVisitor;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerVisitor;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class GraphicManager {
    public interface LayerEditorGraphicVisitor extends LayerVisitor<Node> {
    }

    public interface LayerViewGraphicVisitor extends LayerVisitor<Node> {
    }

    public interface BrushEditorGraphicVisitor extends BrushVisitor<Node> {
    }

    public interface BrushViewGraphicVisitor extends BrushVisitor<Node> {
    }

    public static GraphicManager getInstance() {
        return Pino.getApp().getService(GraphicManager.class);
    }

    final List<LayerEditorGraphicVisitor> layerEditorGraphicVisitors;
    final List<LayerViewGraphicVisitor> layerViewGraphicVisitors;
    final List<BrushEditorGraphicVisitor> brushEditorGraphicVisitorList;
    final List<BrushViewGraphicVisitor> brushViewGraphicVisitors;

    public GraphicManager() {
        layerEditorGraphicVisitors = ServiceLoader.load(LayerEditorGraphicVisitor.class).stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
        layerViewGraphicVisitors = ServiceLoader.load(LayerViewGraphicVisitor.class).stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
        brushEditorGraphicVisitorList = ServiceLoader.load(BrushEditorGraphicVisitor.class).stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
        brushViewGraphicVisitors = ServiceLoader.load(BrushViewGraphicVisitor.class).stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
    }

    public Node getEditorGraphic(LayerObject layer) {
        for (var visitor: layerEditorGraphicVisitors) {
            var graphic = layer.accept(visitor);
            if (graphic != null) {
                return graphic;
            }
        }
        throw new RuntimeException("Not found LayerEditorGraphicVisitor");
    }

    public Node getCellGraphic(LayerObject layer) {
        for (var visitor: layerViewGraphicVisitors) {
            var cell = layer.accept(visitor);
            if (cell != null) {
                return cell;
            }
        }
        throw new RuntimeException("Not found LayerViewGraphicVisitor");
    }

    public Node getEditorGraphic(Brush brush) {
        for (var visitor: brushEditorGraphicVisitorList) {
            var graphic = brush.accept(visitor);
            if (graphic != null) {
                return graphic;
            }
        }
        throw new RuntimeException("Not found BrushEditorGraphicVisitor");
    }

    public Node getCellGraphic(Brush brush) {
        for (var visitor: brushViewGraphicVisitors) {
            var cell = brush.accept(visitor);
            if (cell != null) {
                return cell;
            }
        }
        throw new RuntimeException("Not found BrushViewGraphicVisitor");
    }

    public static void main(String... args) {
        var mgr = new GraphicManager();
        System.out.println(mgr.layerEditorGraphicVisitors);
        System.out.println(mgr.layerViewGraphicVisitors);
        System.out.println(mgr.brushEditorGraphicVisitorList);
        System.out.println(mgr.brushViewGraphicVisitors);
    }
}
