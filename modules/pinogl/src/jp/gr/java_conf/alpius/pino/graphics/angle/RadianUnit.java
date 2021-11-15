package jp.gr.java_conf.alpius.pino.graphics.angle;

final class RadianUnit implements AngleUnit {
    @Override
    public double fromRadian(double radian) {
        return radian;
    }

    @Override
    public double toRadian(double angle) {
        return angle;
    }
}
