package jp.gr.java_conf.alpius.pino.graphics.angle;

// 角度をカプセル化します
public final class Angle {
    public static Angle ZERO = new Angle(0);
    public static Angle RIGHT = new Angle(Math.PI / 2);
    public static Angle HALF = RIGHT.times(2);

    private final double rad;

    private Angle(double rad) {
        this.rad = rad;
    }

    public double getAngle(AngleUnit unit) {
        return unit.fromRadian(rad);
    }

    public Angle plus(Angle angle) {
        return rad(rad + angle.rad);
    }

    public Angle plus(double angle, AngleUnit unit) {
        return rad(rad + unit.toRadian(angle));
    }

    public Angle times(double value) {
        return rad(rad * value);
    }

    public static Angle make(double angle, AngleUnit unit) {
        double rad = unit.toRadian(angle);
        return rad(rad);
    }

    public static Angle rad(double rad) {
        if (rad == 0) {
            return ZERO;
        } else if (rad == Math.PI / 2) {
            return RIGHT;
        } else if (rad == Math.PI) {
            return HALF;
        }
        return new Angle(rad);
    }

    public static Angle deg(double deg) {
        return make(deg, StandardAngleUnit.DEG);
    }
}
