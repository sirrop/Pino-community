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

package jp.gr.java_conf.alpius.pino.ui.actionSystem.plugin;

import javafx.stage.FileChooser;
import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.notification.Notification;
import jp.gr.java_conf.alpius.pino.notification.NotificationType;
import jp.gr.java_conf.alpius.pino.notification.Publisher;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Output implements Action {
    @Override
    public void performAction(ActionEvent e) {
        var p = Pino.getApp().getProject();
        if (p != null) {
            BufferedImage snap = p.getCanvas().snapshot();
            try {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(getExtensionFilters());
                fc.setSelectedExtensionFilter(getExtensionFilters().get(0));
                var file = fc.showSaveDialog(null);
                if (file != null) ImageIO.write(snap, fc.getSelectedExtensionFilter().getDescription(), file);
                else System.err.println("file == null");
            } catch (IOException ex) {
                Notification notification = new Notification(
                        "出力に失敗しました",
                        ex.getMessage(),
                        null,
                        NotificationType.ERROR
                );
                Pino.getApp().getService(Publisher.class).publish(notification);
            }
        }
    }

    private static List<FileChooser.ExtensionFilter> getExtensionFilters() {
        return Arrays.stream(ImageIO.getWriterFormatNames())
                .map(it -> it.toLowerCase(Locale.ROOT))
                .distinct()
                .map(it -> new FileChooser.ExtensionFilter(it, it, "*." + it))
                .collect(Collectors.toList());
    }
}
