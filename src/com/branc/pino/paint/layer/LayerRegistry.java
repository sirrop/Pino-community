package com.branc.pino.paint.layer;

import com.branc.pino.application.ApplicationManager;
import com.branc.pino.paint.layer.internal.FullColorBitmapLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LayerRegistry {
    public static LayerRegistry getInstance() {
        return ApplicationManager.getApp().getService(LayerRegistry.class);
    }

    public LayerRegistry() {
        register(FullColorBitmapLayer.class);
    }

    private final List<Class<? extends LayerObject>> registeredLayerObjects = new ArrayList<>();

    public void register(Class<? extends LayerObject> objectClass) {
        registeredLayerObjects.add(objectClass);
    }

    public List<Class<? extends LayerObject>> getRegisteredLayerObjects() {
        return Collections.unmodifiableList(registeredLayerObjects);
    }
}
