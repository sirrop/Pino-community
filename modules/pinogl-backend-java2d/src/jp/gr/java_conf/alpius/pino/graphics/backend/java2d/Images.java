package jp.gr.java_conf.alpius.pino.graphics.backend.java2d;

import jp.gr.java_conf.alpius.pino.graphics.AlphaType;
import jp.gr.java_conf.alpius.pino.graphics.Image;
import jp.gr.java_conf.alpius.pino.graphics.StandardPixelFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// 現在の画像のエンコード・デコードはImageIOに依存しています
public final class Images {
    public static BufferedImage toSwing(Image image) {
        int w, h;
        BufferedImage res = new BufferedImage(w = image.getWidth(), h = image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
        res.setRGB(0, 0, w, h, pixels, 0, w);
        return res;
    }

    public static Image fromSwing(BufferedImage image) {
        int w, h;
        Image res = new Image(w = image.getWidth(), h = image.getHeight(), StandardPixelFormat.ARGB32, AlphaType.UNPREMUL);
        int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
        res.setRGB(0, 0, w, h, pixels, 0, w);
        return res;
    }

    public static Image read(InputStream in) throws IOException {
        return fromSwing(ImageIO.read(in));
    }

    public static void write(Image image, String format, OutputStream out) throws IOException {
        ImageIO.write(toSwing(image), format, out);
    }
}
