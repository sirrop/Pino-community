package com.branc.pino.io;

import java.nio.file.Path;

public class SimpleSaveState implements SaveState {
    private Path lastSavedPath;

    @Override
    public Path getLastSavedPath() {
        return lastSavedPath;
    }

    @Override
    public void setLastSavedPath(Path path) {
        lastSavedPath = path;
    }
}
