package jp.gr.java_conf.alpius.pino.project;

import java.util.EventListener;

public interface ProjectChangeListener extends EventListener {
    void changed(Project oldValue, Project newValue);
}
