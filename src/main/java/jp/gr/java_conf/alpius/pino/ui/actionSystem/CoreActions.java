package jp.gr.java_conf.alpius.pino.ui.actionSystem;

import groovy.lang.Script;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.SelectionModel;
import javafx.stage.FileChooser;
import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.internal.graphics.Renderer;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts.create_project;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts.not_implemented;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts.open_project;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts.show_env_config;
import org.codehaus.groovy.runtime.InvokerHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

final class CoreActions {
    private CoreActions() {
    }

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
                AddLayer.class,
                RemoveLayer.class
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
            run(not_implemented.class, ACTION_ID);
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
            run(not_implemented.class, ACTION_ID);
        }
    }

    public static class OutputProject extends Action {
        public static final String ACTION_ID = "pino:output-project";
        public static final String DESCRIPTION = "編集中のプロジェクトを出力します";

        public OutputProject() {
            super(ACTION_ID, DESCRIPTION);
        }

        private static final FileChooser.ExtensionFilter PNG = new FileChooser.ExtensionFilter("PNG", "*.png", "*.PNG");
        private static final FileChooser.ExtensionFilter JPEG = new FileChooser.ExtensionFilter("JPEG", "*.jpeg", "*.jpg", "*.JPEG", "*.JPG");

        @Override
        public void performed(ActionEvent e) {
            BufferedImage img = SwingFXUtils.fromFXImage(
                    Renderer.render(ProjectManager.getInstance().getProject(),
                            true),
                    null);
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(
                    PNG, JPEG
            );
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try {
                    ImageIO.write(img, fc.getSelectedExtensionFilter().getDescription(), file);
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
            //Optional.ofNullable(ProjectManager.getInstance().getProject())
            //        .flatMap(it -> it.getService(LayerHistory.class).undoIfCan())
            //        .ifPresent(it -> {
            //            try {
            //                it.getParent().restore(it);
            //            } catch (MementoException mementoException) {
            //                throw new RuntimeException(mementoException);
            //            }
            //        });
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
            //Optional.ofNullable(ProjectManager.getInstance().getProject())
            //        .flatMap(it -> it.getService(LayerHistory.class).redoIfCan())
            //        .ifPresent(it -> {
            //            try {
            //                it.getParent().restore(it);
            //            } catch (MementoException mementoException) {
            //                throw new RuntimeException(mementoException);
            //            }
            //        });
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
            Project project = ProjectManager.getInstance().getProject();
            if (project == null) {
                return;
            }
            SelectionModel<LayerObject> selectionModel = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel();
            if (selectionModel == null) return;
            int index = selectionModel.getSelectedIndex();
            if (index == -1) {
                project.getLayer().add(0, new DrawableLayer());
                selectionModel.selectFirst();
            } else {
                ProjectManager.getInstance().getProject().getLayer().add(index, new DrawableLayer());
                selectionModel.select(index);
            }
        }
    }

    public static class RemoveLayer extends Action {
        public static final String ACTION_ID = "pino:remove-layer";
        public static final String DESCRIPTION = "選択しているレイヤーを取り除きます";

        public RemoveLayer() {
            super(ACTION_ID, DESCRIPTION);
        }

        @Override
        public void performed(ActionEvent e) {
            SelectionModel<LayerObject> selectionModel = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel();
            if (selectionModel == null) return;
            int index = selectionModel.getSelectedIndex();
            if (index != -1) {
                ProjectManager.getInstance().getProject().getLayer().remove(index);
            }
        }
    }
}
