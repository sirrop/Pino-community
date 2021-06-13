package com.branc.pino.project;

import java.util.EventListener;

public interface ProjectChangeListener extends EventListener {
    void changed(Project oldValue, Project newValue);
}
