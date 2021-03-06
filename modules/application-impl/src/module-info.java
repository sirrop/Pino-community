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
import jp.gr.java_conf.alpius.pino.gui.widget.visitor.*;
import jp.gr.java_conf.alpius.pino.io.serializable.action.DeserializeProject;
import jp.gr.java_conf.alpius.pino.io.serializable.action.SerializeProject;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin.*;

module pino.application.impl {
    exports jp.gr.java_conf.alpius.pino.application.impl;
    exports jp.gr.java_conf.alpius.pino.gui;
    exports jp.gr.java_conf.alpius.pino.gui.screen.options;
    exports jp.gr.java_conf.alpius.pino.gui.screen.options.skin;
    exports jp.gr.java_conf.alpius.pino.gui.widget;
    exports jp.gr.java_conf.alpius.pino.gui.widget.skin;
    exports jp.gr.java_conf.alpius.pino.project.impl;
    exports jp.gr.java_conf.alpius.pino.tool.plugin;
    exports jp.gr.java_conf.alpius.pino.ui.actionSystem;
    exports jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin;


    requires pino.application.api;
    requires pino.graphics;
    requires pino.ui;
    requires java.logging;
    requires flogger;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;
    requires org.controlsfx.controls;
    requires com.google.common;

    opens jp.gr.java_conf.alpius.pino.application.impl to javafx.fxml, javafx.graphics;
    opens jp.gr.java_conf.alpius.pino.ui.actionSystem to javafx.fxml;
    opens jp.gr.java_conf.alpius.pino.gui to javafx.graphics, javafx.fxml;
    opens jp.gr.java_conf.alpius.pino.gui.widget to javafx.fxml, javafx.graphics;
    exports jp.gr.java_conf.alpius.pino.bootstrap;
    opens jp.gr.java_conf.alpius.pino.bootstrap to javafx.fxml, javafx.graphics;
    exports jp.gr.java_conf.alpius.pino.gui.widget.visitor;
    opens jp.gr.java_conf.alpius.pino.gui.widget.visitor to javafx.fxml, javafx.graphics;

    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.LayerEditorGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.LayerViewGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.BrushEditorGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.application.impl.GraphicManager.BrushViewGraphicVisitor;
    uses jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
    uses jp.gr.java_conf.alpius.pino.tool.Tool;
    uses GraphicManager.ToolEditorGraphicVisitor;

    provides GraphicManager.LayerEditorGraphicVisitor with DefaultLayerEditorGraphicVisitor;
    provides GraphicManager.LayerViewGraphicVisitor with DefaultLayerViewGraphicVisitor;
    provides GraphicManager.BrushEditorGraphicVisitor with DefaultBrushEditorGraphicVisitor;
    provides GraphicManager.BrushViewGraphicVisitor with DefaultBrushViewGraphicVisitor;
    provides GraphicManager.ToolEditorGraphicVisitor with DefaultToolEditorGraphicVisitor;
    provides Action
            with    ActivateDrawTool, ActivateHandTool, ActivateLassoTool, ActivateRectangleLassoTool, ActivateMoveLayerTool, ActivateEyedropperTool,
                    MakeProject, DeserializeProject, CloseProject, Output, SerializeProject, ShowOptionsAction, Exit,
                    ClearSelection,
                    Undo, Redo,
                    HorizontalFlip, VerticalFlip;
}