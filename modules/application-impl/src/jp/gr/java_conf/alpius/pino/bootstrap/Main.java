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

package jp.gr.java_conf.alpius.pino.bootstrap;

import jp.gr.java_conf.alpius.pino.application.impl.Pino;
import jp.gr.java_conf.alpius.pino.application.impl.Version;
import jp.gr.java_conf.alpius.pino.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {
    private Main() {
    }

    private static final int STARTUP_FAILED = 1;

    public static LocalDateTime startUp;

    public static void main(String[] args) {
        startUp = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

        var flags = Flags.parse(args);

        if (flags.showVersion) {
            System.out.println("Pino for Desktop: " + Version.CURRENT_VERSION);
            System.exit(0);
        }

        try {
            bootstrap(args);
            Pino.launch(Pino.class, args);
        } catch (Throwable t) {
            showMessage("Failed in bootstrap", t);
            System.exit(STARTUP_FAILED);
        }
    }

    private static void bootstrap(String[] args) throws Exception {
        Logger.initLogger();
    }

    private static void showMessage(String title, Throwable cause) {
        StringWriter out = new StringWriter();
        cause.printStackTrace(new PrintWriter(out));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable ignored) {
        }

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText(out.toString());

        JScrollPane scrollPane = new JScrollPane(textPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        int maxHeight = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
        int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        Dimension component = scrollPane.getPreferredSize();
        if (component.height > maxHeight || component.width > maxWidth) {
            scrollPane.setPreferredSize(new Dimension(Math.min(maxWidth, component.width), Math.min(maxHeight, component.height)));
        }
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), scrollPane, title, JOptionPane.ERROR_MESSAGE);
    }
}
