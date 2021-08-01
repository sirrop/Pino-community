package jp.gr.java_conf.alpius.pino.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jp.gr.java_conf.alpius.pino.core.annotaion.Beta;
import jp.gr.java_conf.alpius.pino.ui.skin.OppositeButtonsSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// 作りかけ
@Beta
public class OppositeButtons extends Control {
    public OppositeButtons() {
        getStyleClass().add(Styles.DEFAULT_STYLE_CLASS);
    }

    private StyleableObjectProperty<Orientation> orientation;
    @NotNull
    public final ObjectProperty<Orientation> orientationProperty() {
        if (orientation == null) {
            orientation = new SimpleStyleableObjectProperty<>(Styles.META_ORIENTATION, this, "orientation");
        }
        return orientation;
    }
    public final void setOrientation(@Nullable Orientation value) {
        orientationProperty().set(value);
    }
    @Nullable
    public final Orientation getOrientation() {
        return orientation == null ? null : orientation.get();
    }

    private ObjectProperty<EventHandler<ActionEvent>> primaryOnAction;

    @NotNull
    public final ObjectProperty<EventHandler<ActionEvent>> primaryOnActionProperty() {
        if (primaryOnAction == null) {
            primaryOnAction = new SimpleObjectProperty<>(this, "primaryOnAction");
        }
        return primaryOnAction;
    }

    public final void setPrimaryOnAction(@Nullable EventHandler<ActionEvent> onAction) {
        primaryOnActionProperty().set(onAction);
    }

    @Nullable
    public final EventHandler<ActionEvent> getPrimaryOnAction() {
        return primaryOnAction == null ? null : primaryOnAction.get();
    }


    private ObjectProperty<EventHandler<ActionEvent>> secondaryOnAction;

    @NotNull
    public final ObjectProperty<EventHandler<ActionEvent>> secondaryOnActionProperty() {
        if (secondaryOnAction == null) {
            secondaryOnAction = new SimpleObjectProperty<>(this, "secondaryOnAction");
        }
        return secondaryOnAction;
    }

    public final void setSecondaryOnAction(@Nullable EventHandler<ActionEvent> onAction) {
        secondaryOnActionProperty().set(onAction);
    }

    @Nullable
    public final EventHandler<ActionEvent> getSecondaryOnAction() {
        return secondaryOnAction == null ? null : secondaryOnAction.get();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new OppositeButtonsSkin(this);
    }



    public static List<CssMetaData<? extends Styleable, ?>>  getClassCssMetaData() {
        return Styles.CSS_META_DATA_LIST;
    }

    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    private static final class Styles {
        public static final String DEFAULT_STYLE_CLASS = "opposite-buttons";

        public static final CssMetaData<OppositeButtons, Orientation> META_ORIENTATION = new CssMetaData<>("-fx-orientation", StyleConverter.getEnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            @Override
            public boolean isSettable(OppositeButtons oppositeButtons) {
                return oppositeButtons.orientation == null || !oppositeButtons.orientation.isBound();
            }

            @SuppressWarnings("unchecked")
            @Override
            public StyleableProperty<Orientation> getStyleableProperty(OppositeButtons oppositeButtons) {
                return (StyleableProperty<Orientation>) oppositeButtons.orientationProperty();
            }
        };

        public static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA_LIST = List.of(
                META_ORIENTATION
        );
    }
}
