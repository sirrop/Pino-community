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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class AlphaBlendTest {
    @Test
    public void runTest() throws Exception {
        for (AlphaBlend.Mode mode: AlphaBlend.Mode.values()) {
            composeTest(mode);
        }
    }

    private void composeTest(AlphaBlend.Mode mode) throws Exception {
        composeTest(AlphaBlend.getInstance(mode), Paths.get("test", "image", mode.name().toLowerCase(Locale.ROOT).replace('_', '-') + ".png"));
    }

    private void composeTest(AlphaBlend blend, Path output) throws IOException {
        var lenna = ImageIO.read(Files.newInputStream(Paths.get( "test", "resources", "Lenna.png")));
        var mandrill = ImageIO.read(Files.newInputStream(Paths.get("test", "resources", "mandrill.jpg")));
        var context = blend.createContext(lenna.getColorModel(), mandrill.getColorModel(), null);
        context.compose(lenna.getData(), mandrill.getData(), mandrill.getRaster());
        context.dispose();
        ImageIO.write(mandrill, "png", Files.newOutputStream(output));
    }
}
