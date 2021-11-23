package jp.gr.java_conf.alpius.pino.graphics.transform;

import jp.gr.java_conf.alpius.pino.graphics.angle.Angle;
import jp.gr.java_conf.alpius.pino.graphics.angle.StandardAngleUnit;
import jp.gr.java_conf.alpius.pino.graphics.geom.Point2D;
import jp.gr.java_conf.alpius.pino.graphics.geom.Shape;

import java.util.Arrays;

public class Affine2D extends Transform {
    private final double[] matrix = new double[9];

    private static final double[] IDENTITY_FLAT_MATRIX = {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public static Affine2D makeFromFlatMatrix(double[] flatMatrix) {
        Affine2D affine = new Affine2D();
        System.arraycopy(flatMatrix, 0, affine.matrix, 0, 9);
        return affine;
    }

    public static Affine2D makeFromFlatMatrix(float[] flatMatrix) {
        Affine2D affine = new Affine2D();
        for (int i = 0; i < 9; i++) {
            affine.matrix[i] = flatMatrix[i];
        }
        return affine;
    }

    public static Affine2D makeIdentity() {
        return makeFromFlatMatrix(IDENTITY_FLAT_MATRIX);
    }

    public boolean isIdentity() {
        return Arrays.equals(matrix, IDENTITY_FLAT_MATRIX);
    }

    public void translate(double tx, double ty) {
        matrix[2] = tx * matrix[0] + ty * matrix[1] + matrix[2];
        matrix[5] = tx * matrix[3] + ty * matrix[4] + matrix[5];
    }

    public void scale(double sx, double sy) {
        matrix[0] *= sx;
        matrix[4] *= sy;
        matrix[1] *= sy;
        matrix[3] *= sx;
    }

    public void shear(double shx, double shy) {
        double M0, M1;
        M0 = matrix[0];
        M1 = matrix[1];
        matrix[0] = M0 + M1 * shy;
        matrix[1] = M0 * shx + M1;
        M0 = matrix[3];
        M1 = matrix[4];
        matrix[3] = M0 + M1 * shy;
        matrix[4] = M0 * shx + M1;
    }

    public void rotate(double rad) {
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double M0, M1;
        M0 = matrix[0];
        M1 = matrix[1];
        matrix[0] =  cos * M0 + sin * M1;
        matrix[1] = -sin * M0 + cos * M1;
        M0 = matrix[3];
        M1 = matrix[4];
        matrix[3] =  cos * M0 + sin * M1;
        matrix[4] = -sin * M0 + cos * M1;
    }

    public void rotate(Angle theta) {
        rotate(theta.getAngle(StandardAngleUnit.RAD));
    }

    public void rotate(double rad, double x, double y) {
        translate(x, y);
        rotate(rad);
        translate(-x, -y);
    }

    public void rotate(Angle theta, double x, double y) {
        rotate(theta.getAngle(StandardAngleUnit.RAD), x, y);
    }

    public double[] flatten() {
        return Arrays.copyOf(matrix, 9);
    }

    @Override
    public Transform concatenate(Transform tx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Point2D transform(Point2D p) {
        double x = p.x() * matrix[0] + p.y() * matrix[1] + matrix[2];
        double y = p.x() * matrix[3] + p.y() * matrix[4] + matrix[5];
        return Point2D.at(x, y);
    }

    @Override
    public Shape transform(Shape shape) {
        // todo
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
