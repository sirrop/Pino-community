package jp.gr.java_conf.alpius.pino.internal.project;

import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.service.ServiceContainer;

import java.awt.color.ICC_Profile;
import java.util.List;

@Internal
public class SimpleProject implements Project {
    private final double width;
    private final double height;
    private final ICC_Profile profile;
    private final ServiceContainer service;
    private final List<LayerObject> layer;

    public SimpleProject(double width, double height, ICC_Profile profile, ServiceContainer service, List<LayerObject> layer) {
        this.width = width;
        this.height = height;
        this.profile = profile;
        this.service = service;
        this.layer = layer;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public List<LayerObject> getLayer() {
        return layer;
    }

    @Override
    public LayerObject get(int index) {
        return layer.get(index);
    }

    @Override
    public ICC_Profile getColorProfile() {
        return profile;
    }

    @Override
    public void dispose() {

    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return service.getService(serviceClass);
    }
}