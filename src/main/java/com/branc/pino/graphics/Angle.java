package com.branc.pino.graphics;

/**
 * 角度を表します。度数法か弧度法かということを意識せずに角度を扱うことができます。
 * @since 1.0
 */
public final class Angle {
    private Angle(double value) {
        this.deg = value;
    }

    /**
     * 度数法で0度を表す角度です。
     * @since 1.0
     */
    public static final Angle ZERO = new Angle(0);

    /**
     * 度数法で90度を表す角度です。
     * @since 1.0
     */
    public static final Angle QUARTER = new Angle(90);

    /**
     * 度数法で180度を表す角度です。
     * @since 1.0
     */
    public static final Angle HALF = new Angle(180);

    /**
     * 度数法で270度を表す角度です。
     * @since 1.0
     */
    public static final Angle THREE_QUARTERS = new Angle(270);

    private final double deg;

    /**
     * 度数法で指定した角度と同値な角度オブジェクトを取得します。
     * @param deg 度数法の角度
     * @return 角度
     * @since 1.0
     */
    public static Angle deg(double deg) {
        if (deg == 0) return ZERO;
        return new Angle(deg);
    }

    /**
     * 弧度法で指定した角度と同値な角度オブジェクトを取得します。
     * @param rad 弧度法の角度
     * @return 角度
     * @since 1.0
     */
    public static Angle rad(double rad) {
        if (rad == 0) return ZERO;
        return new Angle((rad / Math.PI) * 180);
    }

    /**
     * 2つの角度を足し、その結果と同値な新しい角度オブジェクトを返します。
     * @param other 足す角度
     * @return 結果
     * @since 1.0
     */
    public Angle plus(Angle other) {
        return new Angle(this.asDeg() + other.asDeg());
    }

    /**
     * この角度から指定された角度を引き、その結果と同値な新しい角度オブジェクトを返します。
     * @param other 引く角度
     * @return 結果
     * @since 1.0
     */
    public Angle minus(Angle other) {
        return new Angle(this.asDeg() - other.asDeg());
    }

    /**
     * この角度を度数法で表したときの値を返します
     * @return 度数法の角度
     * @since 1.0
     */
    public double asDeg() {
        return deg;
    }

    /**
     * この角度を弧度法で表したときの値を返します。
     * @return 弧度法の角度
     * @since 1.0
     */
    public double asRad() {
        return (deg / 180) * Math.PI;
    }


    @Override
    public int hashCode() {
        return (int) Math.floor(13 * deg);
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof Angle) {
            Angle other = (Angle) o;
            return other.deg == deg;
        }
        return false;
    }

    /**
     * このオブジェクトの文字列表現を返します。
     * <p>
     *     フォーマットは、"&lt;この角度を度数法で表したときの値&gt;deg"です。
     * </p>
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format("%fdeg", deg);
    }
}
