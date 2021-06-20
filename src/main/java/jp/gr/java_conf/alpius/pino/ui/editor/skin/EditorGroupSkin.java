package jp.gr.java_conf.alpius.pino.ui.editor.skin;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import jp.gr.java_conf.alpius.pino.ui.editor.EditorGroup;
import jp.gr.java_conf.alpius.pino.ui.editor.FxEditor;

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
        tabPane.getTabs().setAll(editors.stream().map(it -> new Tab(it.getLabel(), it)).collect(Collectors.toList()));

        getChildren().add(tabPane);
    }
}
