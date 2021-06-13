package com.branc.pino.brush;

import com.branc.pino.application.ApplicationManager;
import pino.brush.PencilContext;

import java.util.ArrayList;
import java.util.List;

public class BrushRegistry {
    public static BrushRegistry getInstance() {
        return ApplicationManager.getApp().getService(BrushRegistry.class);
    }

    public BrushRegistry() {
        register(PencilContext.class);
    }

    private final List<Class<? extends BrushContext>> registeredBrushes = new ArrayList<>();

    public void register(Class<? extends BrushContext> klass) {
        if (!registeredBrushes.contains(klass)) registeredBrushes.add(klass);
    }

    public List<Class<? extends BrushContext>> getRegisteredBrushes() {
        return new ArrayList<>(registeredBrushes);
    }
}
