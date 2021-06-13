package com.branc.pino.ui;

import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class KeyMap {
    private final Map<String, KeyCombination> map = new HashMap<>();

    public void put(String actionId, KeyCombination keyCombination) {
        map.put(actionId, keyCombination);
    }

    public void remove(String actionId) {
        map.remove(actionId);
    }

    public KeyCombination get(String actionId) {
        return map.get(actionId);
    }

    public Optional<String> searchFor(KeyEvent e) {
        for (String actionId : map.keySet()) {
            KeyCombination keyCombination = map.get(actionId);
            if (keyCombination.match(e)) return Optional.of(actionId);
        }
        return Optional.empty();
    }

    public Set<String> keySet() {
        return map.keySet();
    }
}
