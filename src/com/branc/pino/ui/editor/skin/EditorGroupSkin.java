package com.branc.pino.ui.editor.skin;

import com.branc.pino.ui.editor.EditorGroup;
import com.branc.pino.ui.editor.FxEditor;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;
import java.util.stream.Collectors;

public class EditorGroupSkin extends SkinBase<EditorGroup> {
    public EditorGroupSkin(EditorGroup control) {
        super(control);
        initialize();
    }

    private void initialize() {
        ObservableList<? extends FxEditor<?>> editors = getSkinnable().getItems();
        TabPane tabPane = new TabPane();
        editors.addListener((ListChangeListener<? super FxEditor<?>>) c -> {
            List<Tab> tabs = editors.stream().map(it -> new Tab(it.getLabel(), it)).collect(Collectors.toList());
            tabPane.getTabs().setAll(tabs);
        });
        getChildren().add(tabPane);
    }
}
