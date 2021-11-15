package jp.gr.java_conf.alpius.pino.graphics.angle;

final class DegreeUnit implements AngleUnit {

    @Override
    public double fromRadian(double radian) {
        return (radian / (2 * Math.PI)) * 360;
    }

    @Override
    public double toRadian(double angle) {
        return (angle / 360) * 2 * Math.PI;
    }
}
