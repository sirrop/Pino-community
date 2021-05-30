package com.branc.pino.ui.actionSystem;

import com.branc.pino.application.ApplicationError;
import com.branc.pino.application.ApplicationManager;
import com.branc.pino.io.ProjectIO;
import com.branc.pino.io.SaveState;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.paint.layer.internal.FullColorBitmapLayer;
import com.branc.pino.project.Project;
import com.branc.pino.project.ProjectManager;
import com.branc.pino.project.ProjectRenderer;
import com.branc.pino.ui.actionSystem.scripts.create_project;
import com.branc.pino.ui.actionSystem.scripts.not_implemented;
import com.branc.pino.ui.actionSystem.scripts.open_project;
import com.branc.pino.ui.actionSystem.scripts.show_env_config;
import groovy.lang.Script;
import javafx.scene.control.SelectionModel;
import javafx.stage.FileChooser;
import org.codehaus.groovy.runtime.InvokerHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

final class CoreActions {
    private CoreActions() {}

    public static List<Class<? extends Action>> get() {
        return List.of(
                CreateProject.class,
                OpenProject.class,
                CloseProject.class,
                SaveProjectAs.class,
                SaveProject.class,
                OutputProject.class,
                ShowEnvConfig.class,
                ShowCopyConfig.class,
                Copy.class,
                ExitApp.class,
                Undo.class,
                Redo.class,
                AddLayer.class
        );
    }

    private static void run(Class<? extends Script> scriptClass) {
        InvokerHelper.runScript(scriptClass, new String[0]);
    }

    private static void run(Class<? extends Script> scriptClass, String... args) {
        InvokerHelper.runScript(scriptClass, args);
    }

    public static class CreateProject extends Action {
        public static final String ACTION_ID = "pino:create-project";
        public static final String DESCRIPTION = "ダイアログを操作して新しいプロジェクトを作成します";

        public CreateProject() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(create_project.class);
        }
    }

    public static class OpenProject extends Action {
        public static final String ACTION_ID = "pino:open-project";
        public static final String DESCRIPTION = "ダイアログを操作してプロジェクトを開きます";

        public OpenProject() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(open_project.class);
        }
    }

    public static class CloseProject extends Action {
        public static final String ACTION_ID = "pino:close-project";
        public static final String DESCRIPTION = "現在開いているプロジェクトを閉じます。未保存の変更は失われます。";

        public CloseProject() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            ProjectManager.getInstance().setProject(null);
        }
    }

    public static class SaveProjectAs extends Action {
        public static final String ACTION_ID = "pino:save-project-as";
        public static final String DESCRIPTION = "現在編集中のプロジェクトを新規保存します。";

        public SaveProjectAs() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("プロジェクト", "*.pino"));
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try {
                    ProjectIO.write(ProjectManager.getInstance().getProject(), file.toPath());
                } catch (IOException ioException) {
                    throw new RuntimeException(ioException);
                }
            }
        }
    }

    public static class SaveProject extends Action {
        public static final String ACTION_ID = "pino:save-project";
        public static final String DESCRIPTION = "現在編集中のプロジェクトを上書き保存します。";

        public SaveProject() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            Project prj = ProjectManager.getInstance().getProject();
            Path lastSaved = prj.getService(SaveState.class).getLastSavedPath();
            if (lastSaved == null) {
                try {
                    ActionRegistry.getInstance().find(SaveProjectAs.ACTION_ID).performed(e);
                } catch (ActionNotFoundException actionNotFoundException) {
                    throw new ApplicationError(actionNotFoundException);
                }
            } else {
                try {
                    ProjectIO.write(prj, lastSaved);
                } catch (IOException ioException) {
                    throw new RuntimeException(ioException);
                }
            }
        }
    }

    public static class OutputProject extends Action {
        public static final String ACTION_ID = "pino:output-project";
        public static final String DESCRIPTION = "編集中のプロジェクトを出力します";

        public OutputProject() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            BufferedImage img = ProjectRenderer.renderBufImg(ProjectManager.getInstance().getProject(), ProjectRenderer.RenderingOption.IGNORE_ROUGH, ProjectRenderer.RenderingOption.IGNORE_INVISIBLE);
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try {
                    if (file.toString().matches(".+\\.png")) {
                        ImageIO.write(img, "png", file);
                    } else {
                        throw new RuntimeException("不明な拡張子です ファイル名：" + file.getName());
                    }
                } catch (IOException ioException) {
                    throw new RuntimeException(ioException);
                }
            }
        }
    }

    public static class ShowEnvConfig extends Action {
        public static final String ACTION_ID = "pino:show-env-config";
        public static final String DESCRIPTION = "環境設定を変更するダイアログを開きます";

        public ShowEnvConfig() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(show_env_config.class);
        }
    }

    public static class ShowCopyConfig extends Action {
        public static final String ACTION_ID = "pino:show-copy-config";
        public static final String DESCRIPTION = "印刷設定を変更するダイアログを開きます";

        public ShowCopyConfig() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(not_implemented.class, ACTION_ID);
        }
    }

    public static class Copy extends Action {
        public static final String ACTION_ID = "pino:copy";
        public static final String DESCRIPTION = "編集中のプロジェクトを印刷します";

        public Copy() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(not_implemented.class, ACTION_ID);
        }
    }

    public static class ExitApp extends Action {
        public static final String ACTION_ID = "pino:exit-app";
        public static final String DESCRIPTION = "アプリケーションを終了します";

        public ExitApp() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            ApplicationManager.getApp().exit();
        }
    }


    public static class Undo extends Action {
        public static final String ACTION_ID = "pino:undo";
        public static final String DESCRIPTION = "変更をひとつ前に戻します";

        public Undo() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(not_implemented.class, ACTION_ID);
        }
    }

    public static class Redo extends Action {
        public static final String ACTION_ID = "pino:redo";
        public static final String DESCRIPTION = "変更をひとつ後に進めます";

        public Redo() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            run(not_implemented.class, ACTION_ID);
        }
    }

    public static class AddLayer extends Action {
        public static final String ACTION_ID = "pino:add-layer";
        public static final String DESCRIPTION = "レイヤーを選択しているレイヤーの上に追加します";

        public AddLayer() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            SelectionModel<LayerObject> selectionModel = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel();
            if (selectionModel == null) return;
            int index = selectionModel.getSelectedIndex();
            if (index == -1) {
                ProjectManager.getInstance().getProject().getLayer().add(0, new FullColorBitmapLayer());
                selectionModel.selectFirst();
            } else {
                ProjectManager.getInstance().getProject().getLayer().add(index, new FullColorBitmapLayer());
                selectionModel.select(index);
            }
        }
    }
}
