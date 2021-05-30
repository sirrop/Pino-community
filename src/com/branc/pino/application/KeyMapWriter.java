package com.branc.pino.application;

import com.branc.pino.ui.KeyMap;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class KeyMapWriter {
    public void store(KeyMap map, Path path) throws IOException {
        PrintWriter out = new PrintWriter(Files.newBufferedWriter(path));

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.println("<keymap>");
        List<String> actions = new ArrayList<>(map.keySet());
        Collections.sort(actions);
        for (String action: actions) {
            out.printf("\t<shortcut action=\"%s\" key-combination=\"%s\" />\n", action, map.get(action));
        }
        out.println("</keymap>");
        out.close();
    }
}
