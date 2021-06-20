package jp.gr.java_conf.alpius.pino.project;

import jp.gr.java_conf.alpius.pino.service.MutableServiceContainer;
import jp.gr.java_conf.alpius.pino.service.ServiceContainer;
import jp.gr.java_conf.alpius.pino.service.SimpleServiceContainer;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.gr.java_conf.alpius.pino.internal.project.SimpleProject;

import java.awt.color.ICC_Profile;
import java.util.List;

public interface Project extends ServiceContainer, Disposable {
    static Project create(double width, double height, ICC_Profile profile) {
        MutableServiceContainer serviceContainer = new SimpleServiceContainer();
        ObservableList<LayerObject> layer = FXCollections.observableArrayList();
        return new SimpleProject(width, height, profile, serviceContainer, layer);
    }

    double getWidth();

    double getHeight();

    List<LayerObject> getLayer();

    LayerObject get(int index);

    ICC_Profile getColorProfile();
}
