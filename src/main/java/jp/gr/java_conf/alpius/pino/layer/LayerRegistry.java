package jp.gr.java_conf.alpius.pino.layer;

import jp.gr.java_conf.alpius.pino.application.ApplicationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LayerRegistry {
    public static LayerRegistry getInstance() {
        return ApplicationManager.getApp().getService(LayerRegistry.class);
    }

    private final List<Class<? extends LayerObject>> registeredLayerObjects = new ArrayList<>();

    public void register(Class<? extends LayerObject> objectClass) {
        registeredLayerObjects.add(objectClass);
    }

    public List<Class<? extends LayerObject>> getRegisteredLayerObjects() {
        return Collections.unmodifiableList(registeredLayerObjects);
    }
}