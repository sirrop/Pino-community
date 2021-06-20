package jp.gr.java_conf.alpius.pino.project;

import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.core.util.Disposer;

import java.util.ArrayList;
import java.util.List;

public final class ProjectManager {

    public static ProjectManager getInstance() {
        return ApplicationManager.getApp().getService(ProjectManager.class);
    }

    private Project project;
    private final List<ProjectChangeListener> listeners = new ArrayList<>();

    public void addListener(ProjectChangeListener listener) {
        listeners.add(listener);
    }

    public void addListener(ProjectChangeListener listener, Disposable parent) {
        addListener(listener);
        Disposer.registerDisposable(parent, () -> removeListener(listener));
    }

    public void removeListener(ProjectChangeListener listener) {
        listeners.remove(listener);
    }

    private void fire(Project old, Project n) {
        listeners.parallelStream().forEach(it -> it.changed(old, n));
    }

    public void setProject(Project project) {
        var old = this.project;
        this.project = project;
        fire(old, project);
    }

    public Project getProject() {
        return project;
    }
}
