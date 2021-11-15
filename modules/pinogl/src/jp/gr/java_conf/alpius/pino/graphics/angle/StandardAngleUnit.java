package jp.gr.java_conf.alpius.pino.graphics.angle;

public enum StandardAngleUnit implements AngleUnit {
    RAD(new RadianUnit()),
    DEG(new RadianUnit());

    private final AngleUnit impl;

    StandardAngleUnit(AngleUnit impl) {
        this.impl = impl;
    }


    @Override
    public double fromRadian(double radian) {
        return impl.fromRadian(radian);
    }

    @Override
    public double toRadian(double angle) {
        return impl.toRadian(angle);
    }
}
