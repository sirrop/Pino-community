/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.graphics;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

public class G2DTest {
    @Test
    public void drawImageTrans() throws Exception {
        var lenna = ImageIO.read(Files.newInputStream(Paths.get("test", "resources", "Lenna.png")));
        var image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        g.drawImage(lenna, 0, 0, null);
        ImageIO.write(image, "png", Files.newOutputStream(Paths.get("test", "resources", "DrawImage-NoTrans.png")));
        clear(g, 200, 200);
        g.translate(-100, 0);
        g.drawImage(lenna, 0, 0, null);
        ImageIO.write(image, "png", Files.newOutputStream(Paths.get("test", "resources", "DrawImage-WithTrans.png")));
        g.dispose();
    }

    private static void clear(Graphics2D g, int w, int h) {
        g.setComposite(AlphaComposite.Src);
        g.setPaint(new Color(0, true));
        g.fillRect(0, 0, w, h);
    }
}
