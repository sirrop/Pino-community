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

package jp.gr.java_conf.alpius.pino.gui.screen.options.skin;

import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import jp.gr.java_conf.alpius.pino.gui.screen.options.Option;
import jp.gr.java_conf.alpius.pino.gui.screen.options.OptionScreen;

public class OptionScreenSkin extends SkinBase<OptionScreen> {
    public OptionScreenSkin(OptionScreen screen) {
        super(screen);
        initialize();
    }

    private void initialize() {
        var screen = getSkinnable();

        var splitPane = new SplitPane();
        splitPane.setDividerPositions(0.3);

        var placeholder = new StackPane(new Label("左側のメニューから項目を選択してください"));

        ListView<Option> optionsMenu = new ListView<>();
        optionsMenu.setCellFactory(listView -> new OptionCell());
        optionsMenu.setItems(screen.getItems());

        splitPane.getItems().addAll(optionsMenu, placeholder);

        optionsMenu.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> splitPane.getItems().set(1, newValue.createNode()));

        getChildren().add(splitPane);
    }

    private static class OptionCell extends ListCell<Option> {
        @Override
        public void updateItem(Option item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item.title());
            }
        }
    }
}
