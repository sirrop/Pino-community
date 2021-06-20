package jp.gr.java_conf.alpius.pino.application;

import jp.gr.java_conf.alpius.pino.ui.KeyMap;
import jp.gr.java_conf.alpius.pino.ui.KeyMap;
import javafx.scene.input.KeyCombination;
import jp.gr.java_conf.alpius.pino.ui.KeyMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DefaultHandler2;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class KeyMapLoader extends DefaultHandler2 {
    private KeyMap map;

    public KeyMap load(Path path) throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            map = new KeyMap();
            parser.parse(Files.newInputStream(path), this);
            return map;
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        startElement2(uri, localName, qName, (Attributes2) attributes);
    }

    private void startElement2(String uri, String localName, String qName, Attributes2 attr) {
        if (qName.equals("keymap")) return;
        if (qName.equals("shortcut")) {
            String action = attr.getValue("action");
            String keyCombo = attr.getValue("key-combination");
            map.put(action, KeyCombination.valueOf(keyCombo));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }
}
