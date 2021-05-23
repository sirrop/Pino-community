package com.branc.pino.ui;

import com.branc.pino.ui.actionSystem.Action;
import com.branc.pino.ui.actionSystem.ActionEvent;
import com.branc.pino.ui.actionSystem.ActionNotFoundException;
import com.branc.pino.ui.actionSystem.ActionRegistry;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DefaultHandler2;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MenuBarInitializer extends DefaultHandler2 {
    public static final String DEFAULT_FILE = "data/MenuConfig.xml";

    private final MenuBar menuBar;
    private final LinkedList<Menu> menus = new LinkedList<>();

    private final List<Stacktrace> stacktraces = new ArrayList<>();

    public MenuBarInitializer(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public static class Stacktrace {
        public Stacktrace(String text, String message) {
            this.text = text;
            this.message = message;
        }

        public final String text;
        public final String message;
    }

    public static final class MenuBarDef {
        private MenuBarDef() {}

        public static final String Q_NAME = "menu-bar";
        public static final String ATTR_VISIBLE = "visible";

        public static boolean isVisible(Attributes2 attr) {
            String visible = attr.getValue(ATTR_VISIBLE);
            if (visible.isEmpty()) {
                return true;
            }
            return Boolean.parseBoolean(attr.getValue(ATTR_VISIBLE));
        }

        public static void configure(MenuBar menuBar, Attributes2 attr) {
            String visible = attr.getValue(ATTR_VISIBLE);
            if (visible.isEmpty()) {
                return;
            }
            menuBar.setVisible(isVisible(attr));
            menuBar.setManaged(isVisible(attr));
        }
    }

    public static final class MenuItemDef {
        private MenuItemDef() {}

        public static final String Q_NAME = "menu-item";
        public static final String ATTR_TEXT = "text";
        public static final String ATTR_ACTION_ID = "actionId";

        public static MenuItem createMenuItem(Attributes2 attr) throws ActionNotFoundException {
            MenuItem menuItem = new MenuItem();
            menuItem.setText(attr.getValue(ATTR_TEXT));
            Action action = ActionRegistry.getInstance().find(attr.getValue(ATTR_ACTION_ID));
            menuItem.setOnAction(e -> action.performed(new ActionEvent(menuItem)));
            return menuItem;
        }
    }

    public static final class MenuDef {
        private MenuDef() {}

        public static final String Q_NAME = "menu";
        public static final String ATTR_TEXT = "text";

        public static Menu createMenu(Attributes2 attr) {
            Menu menu = new Menu();
            menu.setText(attr.getValue(ATTR_TEXT));
            return menu;
        }
    }

    public static void initialize(MenuBar menuBar, String configFile) {
        MenuBarInitializer loader = new MenuBarInitializer(menuBar);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(configFile), loader);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize(MenuBar menuBar) {
        initialize(menuBar, DEFAULT_FILE);
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        startElement2(uri, localName, qName, (Attributes2) attributes);
    }

    public void startElement2(String uri, String localName, String qName, Attributes2 attributes) {
        switch (qName) {
            case MenuBarDef.Q_NAME: {
                MenuBarDef.configure(menuBar, attributes);
                break;
            }
            case MenuItemDef.Q_NAME: {
                try {
                    MenuItem item = MenuItemDef.createMenuItem(attributes);
                    menus.getLast().getItems().add(item);
                } catch (ActionNotFoundException e) {
                    stacktraces.add(new Stacktrace(attributes.getValue(MenuItemDef.ATTR_TEXT), "An action was not found."));
                }
                break;
            }
            case MenuDef.Q_NAME: {
                Menu menu = MenuDef.createMenu(attributes);
                menus.add(menu);
                break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals(MenuDef.Q_NAME)) {
            Menu menu = menus.removeLast();
            if (!menus.isEmpty()) {
                menus.getLast().getItems().add(menu);
            } else {
                menuBar.getMenus().add(menu);
            }
        }
        if (qName.equals(MenuBarDef.Q_NAME) && !stacktraces.isEmpty()) {
            System.err.println("以下のメニューのロードに失敗しました");
            for (Stacktrace s: stacktraces) {
                System.err.printf("%s\n", s.text);
                System.err.printf("     Cause: %s\n", s.message);
            }
        }
    }
}
