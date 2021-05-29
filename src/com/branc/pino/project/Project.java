package com.branc.pino.project;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.io.SaveState;
import com.branc.pino.io.SimpleSaveState;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.internal.SimpleProject;
import com.branc.pino.service.MutableServiceContainer;
import com.branc.pino.service.ServiceContainer;
import com.branc.pino.service.SimpleServiceContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.color.ICC_Profile;
import java.util.List;

public interface Project extends ServiceContainer, Disposable {
    static Project create(double width, double height, ICC_Profile profile) {
        MutableServiceContainer serviceContainer = new SimpleServiceContainer();
        ObservableList<LayerObject> layer = FXCollections.observableArrayList();
        Project res = new SimpleProject(width, height, profile, serviceContainer, layer);

        serviceContainer.register(SaveState.class, new SimpleSaveState());

        return res;
    }
    double getWidth();
    double getHeight();
    List<LayerObject> getLayer();
    LayerObject get(int index);
    ICC_Profile getColorProfile();
}
