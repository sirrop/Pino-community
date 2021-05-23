package com.branc.pino.application;

import com.branc.pino.paint.brush.BrushManager;
import com.branc.pino.paint.brush.BrushRegistry;
import com.branc.pino.paint.layer.LayerRegistry;
import com.branc.pino.project.ProjectManager;
import com.branc.pino.service.MutableServiceContainer;
import com.branc.pino.ui.actionSystem.ActionRegistry;

final class CoreServices {
    public CoreServices() {}

    public static void load(MutableServiceContainer serviceContainer) {
        serviceContainer.register(ActionRegistry.class, new ActionRegistry());
        serviceContainer.register(ProjectManager.class, new ProjectManager());
        serviceContainer.register(LayerRegistry.class, new LayerRegistry());
        serviceContainer.register(BrushRegistry.class, new BrushRegistry());
        serviceContainer.register(BrushManager.class, new BrushManager());
    }
}
