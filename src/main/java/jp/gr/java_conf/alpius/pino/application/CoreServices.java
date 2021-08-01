package jp.gr.java_conf.alpius.pino.application;

import javafx.collections.FXCollections;
import jp.gr.java_conf.alpius.pino.brush.BrushManager;
import jp.gr.java_conf.alpius.pino.brush.BrushRegistry;
import jp.gr.java_conf.alpius.pino.layer.LayerRegistry;
import jp.gr.java_conf.alpius.pino.notification.NotificationCenter;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import jp.gr.java_conf.alpius.pino.service.MutableServiceContainer;
import jp.gr.java_conf.alpius.pino.ui.KeyMap;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

final class CoreServices {
    public CoreServices() {
    }

    public static void load(MutableServiceContainer serviceContainer) {
        serviceContainer.register(ActionRegistry.class, new ActionRegistry());
        serviceContainer.register(ProjectManager.class, new ProjectManager());
        serviceContainer.register(LayerRegistry.class, new LayerRegistry());
        serviceContainer.register(BrushRegistry.class, new BrushRegistry());
        serviceContainer.register(BrushManager.class, new BrushManager(FXCollections.observableArrayList()));
        serviceContainer.register(NotificationCenter.class, new NotificationCenter());
        serviceContainer.register(KeyMap.class, loadKeyMap());
    }

    private static KeyMap loadKeyMap() {
        Path path = Paths.get(System.getProperty("appDir", "."), "data", "KeyMap.xml");
        KeyMapLoader loader = new KeyMapLoader();
        try {
            return loader.load(path);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
