package com.branc.pino.application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class AppConfig {
    private AppConfig() {
    }

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

    public void save() throws IOException {
        File file = new File(System.getProperty("appDir", ".") + "/data/config.properties");
        if (!file.exists()) file.createNewFile();
        properties.store(new PrintWriter(file), "");
    }

    private void set(String key, String value) {
        properties.setProperty(key, value);
    }

    private void set(String key, int value) {
        set(key, String.valueOf(value));
    }

    private void set(String key, long value) {
        set(key, String.valueOf(value));
    }

    private void set(String key, float value) {
        set(key, String.valueOf(value));
    }

    private void set(String key, double value) {
        set(key, String.valueOf(value));
    }

    private void set(String key, boolean value) {
        set(key, String.valueOf(value));
    }

    public void setStageSize(double preset) {
        if (preset == USE_PREF_SIZE) {
            set("scene.size", "use-pref-size");
        } else if (preset == FULL_SCREEN) {
            set("scene.size", "full-screen");
        } else if (preset == COMPUTE_MAX_SIZE) {
            set("scene.size", "compute-max-size");
        } else {
            throw new IllegalArgumentException("Unknown size type");
        }
    }

    public void setStateSize(double w, double h) {
        if (w < 0 || h < 0) throw new IllegalArgumentException();
        set("scene.size", w + "," + h);
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

    public void setCanvasFps(double value) {
        if (value > 0 && Double.isFinite(value)) {
            set("canvas.fps", String.valueOf(value));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getZoomRate() {
        String value = properties.getProperty("canvas.zoom");
        if (value == null || !value.matches("\\s+(\\.\\d+)?")) return 0.0025;
        return Double.parseDouble(value);
    }

    public void setZoomRate(double rate) {
        if (rate > 0 && Double.isFinite(rate)) {
            set("canvas.zoom", rate);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
