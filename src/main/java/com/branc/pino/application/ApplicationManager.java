package com.branc.pino.application;

public final class ApplicationManager {
    private ApplicationManager() {
    }

    private static Pino app;

    static void setApplication(Pino application) {
        if (application == null) {
            throw new NullPointerException("application is null");
        }
        app = application;
    }

    public static Pino getApp() {
        if (app == null) {
            throw new IllegalStateException("application is null");
        }
        return app;
    }
}
