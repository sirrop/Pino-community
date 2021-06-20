package jp.gr.java_conf.alpius.pino.tool;

import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public interface Tool extends Disposable {
    default void mousePressed(MouseEvent e) {
    }

    default void mouseDragged(MouseEvent e) {
    }

    default void mouseReleased(MouseEvent e) {
    }

    default void mouseClicked(MouseEvent e) {
    }

    default void scroll(ScrollEvent e) {
    }

    default void scrollStart(ScrollEvent e) {
    }

    default void scrollFinished(ScrollEvent e) {
    }
}
