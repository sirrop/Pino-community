package jp.gr.java_conf.alpius.pino.ui.config;

public class CanvasConfig implements Config {
    private final double initialFps;
    private final double initialZoomRate;

    public CanvasConfig(double fps, double zoomRate) {
        this.fps = initialFps = fps;
        this.zoomRate = initialZoomRate = zoomRate;
    }

    private double fps;
    private double zoomRate;

    public void setFps(double fps) {
        this.fps = fps;
    }

    public double getFps() {
        return fps;
    }

    public void setZoomRate(double zoomRate) {
        this.zoomRate = zoomRate;
    }

    public double getZoomRate() {
        return zoomRate;
    }

    @Override
    public void applyChange() {

    }
}
