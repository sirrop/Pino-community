package com.branc.pino.project;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.internal.SimpleProject;
import com.branc.pino.service.ServiceContainer;
import com.branc.pino.service.SimpleServiceContainer;
import javafx.collections.FXCollections;

import java.awt.color.ICC_Profile;
import java.util.List;

public interface Project extends ServiceContainer, Disposable {
    static Project create(double width, double height, ICC_Profile profile) {
        return new SimpleProject(width, height, profile, new SimpleServiceContainer(), FXCollections.observableArrayList());
    }
    double getWidth();
    double getHeight();
    List<LayerObject> getLayer();
    LayerObject get(int index);
    ICC_Profile getColorProfile();
}
