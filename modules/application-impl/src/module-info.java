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

import jp.gr.java_conf.alpius.pino.application.impl.GraphicManager;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin.*;
import jp.gr.java_conf.alpius.pino.window.impl.DefaultBrushEditorGraphicVisitor;
import jp.gr.java_conf.alpius.pino.window.impl.DefaultBrushViewGraphicVisitor;
import jp.gr.java_conf.alpius.pino.window.impl.DefaultLayerEditorGraphicVisitor;
import jp.gr.java_conf.alpius.pino.window.impl.DefaultLayerViewGraphicVisitor;

module pino.application.impl {
    exports jp.gr.java_conf.alpius.pino.application.impl;

    requires pino.application.api;
    requires pino.graphics;
    requires pino.ui;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;
    requires org.controlsfx.controls;

    opens jp.gr.java_conf.alpius.pino.window.impl to javafx.graphics, javafx.fxml;
    opens jp.gr.java_conf.alpius.pino.application.impl to javafx.fxml, javafx.graphics;
    opens jp.gr.java_conf.alpius.pino.ui.actionSystem to javafx.fxml;

    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.LayerEditorGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.LayerViewGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.BrushEditorGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.BrushViewGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
    uses jp.gr.java_conf.alpius.pino.tool.Tool;

    provides GraphicManager.LayerEditorGraphicVisitor with DefaultLayerEditorGraphicVisitor;
    provides GraphicManager.LayerViewGraphicVisitor with DefaultLayerViewGraphicVisitor;
    provides GraphicManager.BrushEditorGraphicVisitor with DefaultBrushEditorGraphicVisitor;
    provides GraphicManager.BrushViewGraphicVisitor with DefaultBrushViewGraphicVisitor;
    provides Action
            with    ActivateDrawTool, ActivateHandTool, ActivateLassoTool, ActivateRectangleLassoTool, ActivateMoveLayerTool,
                    MakeProject, CloseProject, Output, Exit, ClearSelection,
                    Undo, Redo,
                    HorizontalFlip, VerticalFlip;
}