package com.branc.pino.application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class AppConfig {
    private AppConfig() {}

    public static final double USE_PREF_SIZE = -1;
    public static final double FULL_SCREEN = -2;
    public static final double COMPUTE_MAX_SIZE = -3;
    public static final String ROOT_DEFAULT = "default";

    private static final String REGEXP_STAGE_SIZE = "(\\s*compute-max-size\\s*)|(\\s*full-screen\\s*)|(\\s*use-pref-size\\s*)|(\\s*[1-9][0-9]*(\\.[0-9]+)?\\s*,\\s*[1-9][0-9]*(\\.[0-9]+)?\\s*)";

    private Properties properties;

    public static AppConfig loadConfig(String appDir) {
        var res = new AppConfig();
        res.properties = new Properties();
        try {
            Path path = Paths.get(appDir, "data", "config.properties");
            if (Files.isReadable(path)) {
                res.properties.load(Files.newBufferedReader(path, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public double getStageWidth() {
        String value = properties.getProperty("scene.size", "use-pref-size");
        if (value.matches(REGEXP_STAGE_SIZE)) {
            switch (value) {
                case "use-pref-size":
                    return USE_PREF_SIZE;
                case "full-screen":
                    return FULL_SCREEN;
                case "compute-max-size":
                    return COMPUTE_MAX_SIZE;
                default:
                    var val = value.split(",")[0];
                    val = val.replace(" ", "");
                    return Double.parseDouble(val);
            }
        }
        return USE_PREF_SIZE;
    }

    public double getStageHeight() {
        String value = properties.getProperty("scene.size", "use-pref-size");
        if (value.matches(REGEXP_STAGE_SIZE)) {
            switch (value) {
                case "use-pref-size":
                    return USE_PREF_SIZE;
                case "full-screen":
                    return FULL_SCREEN;
                case "compute-max-size":
                    return COMPUTE_MAX_SIZE;
                default:
                    var val = value.split(",")[1];
                    val = val.replace(" ", "");
                    return Double.parseDouble(val);
            }
        }
        return USE_PREF_SIZE;
    }


    public String getRootFXML() {
        return properties.getProperty("root", ROOT_DEFAULT);
    }

    public double getCanvasFps() {
        String value = properties.getProperty("canvas.fps");
        if (value == null || !value.matches("\\d+(\\.\\d+)?")) {
            return 60;
        }
        return Double.parseDouble(value);
    }

    public double getZoomRate() {
        String value = properties.getProperty("canvas.zoom");
        if (value == null || !value.matches("\\s+(\\.\\d+)?")) return 0.0025;
        return Double.parseDouble(value);
    }
}
