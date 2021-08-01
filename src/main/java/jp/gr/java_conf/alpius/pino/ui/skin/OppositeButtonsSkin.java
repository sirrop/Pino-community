package jp.gr.java_conf.alpius.pino.ui.skin;

import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import jp.gr.java_conf.alpius.pino.ui.OppositeButtons;

public class OppositeButtonsSkin extends SkinBase<OppositeButtons> {
    private final StackPane buttonArea = new StackPane();
    private final StackPane primaryButton = new StackPane();
    private final Region primaryButtonArrow = new Region();
    private final StackPane secondaryButton = new StackPane();
    private final Region secondaryButtonArrow = new Region();

    public OppositeButtonsSkin(OppositeButtons control) {
        super(control);
        Styles.init(this);
        updateListeners();
        initialize(control);
    }

    private void initialize(OppositeButtons skinnable) {
        layoutButtons(skinnable.getOrientation());
        skinnable.orientationProperty().addListener((obs, oldValue, newValue) -> layoutButtons(newValue));
        primaryButton.getChildren().add(primaryButtonArrow);
        secondaryButton.getChildren().add(secondaryButtonArrow);
        buttonArea.getChildren().setAll(primaryButton, secondaryButton);
        getChildren().setAll(buttonArea);
    }

    private void updateListeners() {
        primaryButton.setOnMouseClicked(e -> {
            var handler = getSkinnable().getPrimaryOnAction();
            if (handler != null) {
                handler.handle(new ActionEvent(primaryButton, primaryButton));
            }
        });
        secondaryButton.setOnMouseClicked(e -> {
            var handler = getSkinnable().getSecondaryOnAction();
            if (handler != null) {
                handler.handle(new ActionEvent(secondaryButton, secondaryButton));
            }
        });
    }

    private void layoutButtons(Orientation orientation) {
        if (orientation == null) {
            orientation = Orientation.HORIZONTAL;
        }
        switch (orientation) {
            case HORIZONTAL: {
                StackPane.setAlignment(primaryButton, Pos.CENTER_LEFT);
                StackPane.setAlignment(secondaryButton, Pos.CENTER_RIGHT);
                break;
            }
            case VERTICAL: {
                StackPane.setAlignment(primaryButton, Pos.BOTTOM_CENTER);
                StackPane.setAlignment(secondaryButton, Pos.TOP_CENTER);
                break;
            }
        }
    }

    private boolean isHorizontal() {
        var orientation = getSkinnable().getOrientation();
        return orientation == null || orientation == Orientation.HORIZONTAL;
    }

    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double primaryArrowWidth = snapSizeX(primaryButtonArrow.prefWidth(-1));
        final double secondaryArrowWidth = snapSizeX(secondaryButtonArrow.prefWidth(-1));
        final double primaryButtonWidth
                      = primaryButton.snappedLeftInset() +
                        primaryArrowWidth +
                        primaryButton.snappedRightInset();
        final double secondaryButtonWidth
                      = secondaryButton.snappedLeftInset() +
                        secondaryArrowWidth +
                        secondaryButton.snappedRightInset();
        double totalWidth;
        if (isHorizontal()) {
            totalWidth = primaryButtonWidth + secondaryButtonWidth;
        } else {
            totalWidth = Math.max(primaryButtonWidth, secondaryButtonWidth);
        }
        return leftInset + totalWidth + rightInset;
    }

    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double primaryArrowHeight = snapSizeY(primaryButtonArrow.prefHeight(-1));
        final double secondaryArrowHeight = snapSizeY(secondaryButtonArrow.prefHeight(-1));
        final double primaryButtonHeight
                      = primaryButton.snappedTopInset() +
                        primaryArrowHeight +
                        primaryButton.snappedBottomInset();
        final double secondaryButtonHeight
                      = secondaryButton.snappedTopInset() +
                        secondaryArrowHeight +
                        secondaryButton.snappedBottomInset();
        double totalHeight;
        if (isHorizontal()) {
            totalHeight = Math.max(primaryButtonHeight, secondaryButtonHeight);
        } else {
            totalHeight = primaryButtonHeight + secondaryButtonHeight;
        }
        return topInset + totalHeight + bottomInset;
    }


    private static final class Styles {
        public static void init(OppositeButtonsSkin skin) {
            skin.buttonArea.getStyleClass().add("button-area");
            skin.primaryButton.getStyleClass().add("primary-button");
            skin.primaryButton.setFocusTraversable(false);
            skin.primaryButtonArrow.getStyleClass().add("arrow");
            skin.primaryButtonArrow.setFocusTraversable(false);
            skin.primaryButtonArrow.setMouseTransparent(true);
            skin.secondaryButton.getStyleClass().add("secondary-button");
            skin.secondaryButton.setFocusTraversable(false);
            skin.secondaryButton.getStyleClass().add("arrow");
            skin.secondaryButtonArrow.setFocusTraversable(false);
            skin.secondaryButtonArrow.setMouseTransparent(true);
        }
    }
}
