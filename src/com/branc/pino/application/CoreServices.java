package com.branc.pino.application;

import com.branc.pino.notification.NotificationCenter;
import com.branc.pino.paint.brush.BrushContext;
import com.branc.pino.paint.brush.BrushManager;
import com.branc.pino.paint.brush.BrushRegistry;
import com.branc.pino.paint.brush.internal.PencilContext;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.paint.layer.LayerRegistry;
import com.branc.pino.paint.layer.internal.FullColorBitmapLayer;
import com.branc.pino.project.ProjectManager;
import com.branc.pino.service.MutableServiceContainer;
import com.branc.pino.ui.KeyMap;
import com.branc.pino.ui.actionSystem.ActionRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

final class CoreServices {
    public CoreServices() {}

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
