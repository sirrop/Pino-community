package com.branc.pino.ui.editor.skin;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.ui.attr.NumberAttribute;
import com.branc.pino.ui.attr.StringAttribute;
import com.branc.pino.ui.attr.ViewType;
import com.branc.pino.ui.editor.EditorTarget;
import com.branc.pino.ui.editor.InvocationException;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class PropertyEditorBuilder {

    public ViewType getDefaultViewType(Class<?> type) {
        if (type == int.class || type == long.class || type == float.class || type == double.class) return ViewType.SLIDER;
        if (type == String.class) return ViewType.TEXTAREA;
        if (type == boolean.class) return ViewType.CHECK_BOX;
        if (type == Color.class) return ViewType.COLOR_CHOOSER;
        throw new IllegalArgumentException("There is no default type.");
    }

    public Node createPropertyEditor(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        ViewType type = (ViewType) desc.getValue(ViewType.KEY_VIEW_TYPE);
        if (type == null) {
            type = getDefaultViewType(desc.getPropertyType());
        }

        switch (type) {
            case SLIDER: return slider(desc, target, parent);
            case TEXTAREA: return textarea(desc, target, parent);
            case CHECK_BOX: return checkBox(desc, target, parent);
            case COLOR_CHOOSER: return colorChooser(desc, target, parent);
            default: throw new IllegalArgumentException("Can't create property editor.");
        }
    }

    private Node slider(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(desc.getName());
        Slider slider = new Slider();
        slider.setMin((double) desc.getValue(NumberAttribute.MIN.getAttributeName()));
        slider.setMax(((double) desc.getValue(NumberAttribute.MAX.getAttributeName())));
        slider.setBlockIncrement((double) desc.getValue(NumberAttribute.BLOCK_INCREMENT.getAttributeName()));
        javafx.scene.control.TextField textField = new TextField();
        TextFormatter<Number> formatter;
        try {
            formatter = new TextFormatter<>(new NumberStringConverter(), (Number) desc.getReadMethod().invoke(target));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InvocationException(e);
        }
        textField.setTextFormatter(formatter);
        slider.valueProperty().bindBidirectional(formatter.valueProperty());

        PropertyChangeListener propertyChangeListener = e -> {
            if (e.getPropertyName().equals(LayerObject.OPACITY)) slider.setValue((float) e.getNewValue());
        };

        target.addListener(propertyChangeListener, parent);
        slider.valueProperty().addListener((obs, old, newValue) -> {
            try {
                desc.getWriteMethod().invoke(target, newValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InvocationException(e);
            }
        });

        return new HBox(label, slider, textField);
    }

    private Node textarea(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        try {
            Label label = new Label(desc.getDisplayName());
            String regexp = (String) Objects.requireNonNullElse(desc.getValue(StringAttribute.REGEXP.getAttributeName()), ".*");
            TextField field = new TextField();
            field.setText((String) desc.getReadMethod().invoke(target));
            TextFormatter<?> formatter = new TextFormatter<>(c -> {
                if (c.getControlNewText().matches(regexp)) {
                    return c;
                } else {
                    return null;
                }
            });
            field.setTextFormatter(formatter);
            formatter.valueProperty().addListener((obs, old, newValue) -> {
                try {
                    desc.getWriteMethod().invoke(target, newValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvocationException(e);
                }
            });
            PropertyChangeListener lis = e -> {
                if (e.getPropertyName().equals(desc.getName())) {
                    field.setText((String) e.getNewValue());
                }
            };
            target.addListener(lis, parent);
            return new HBox(label, field);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvocationException(e);
        }
    }

    private Node checkBox(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        CheckBox checkBox = new CheckBox();
        try {
            checkBox.setSelected((boolean) desc.getReadMethod().invoke(target));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InvocationException(e);
        }
        checkBox.setText(desc.getDisplayName());
        checkBox.selectedProperty().addListener((obs, old, newValue) -> {
            try {
                desc.getWriteMethod().invoke(target, newValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InvocationException(e);
            }
        });
        PropertyChangeListener listener = e -> {
            if (e.getPropertyName().equals(desc.getName())) {
                checkBox.setSelected((boolean) e.getNewValue());
            }
        };
        target.addListener(listener, parent);
        return checkBox;
    }

    private Node colorChooser(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        Label label = new Label(desc.getDisplayName());
        ColorPicker picker = new ColorPicker();
        PropertyChangeListener lis = e -> {
            if (e.getPropertyName().equals(desc.getName())) {
                try {
                    java.awt.Color c = (java.awt.Color) desc.getReadMethod().invoke(target);
                    picker.setValue(toFX(c));
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new InvocationException(ex);
                }
            }
        };
        target.addListener(lis, parent);
        picker.valueProperty().addListener((obs, old, newValue) -> {
            try {
                desc.getWriteMethod().invoke(target, toAWT(newValue));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InvocationException(e);
            }
        });
        return new HBox(label, picker);
    }

    private java.awt.Color toAWT(javafx.scene.paint.Color color) {
        return new java.awt.Color((float) color.getRed(),
                (float)color.getGreen(),
                (float)color.getBlue(),
                (float)color.getOpacity());
    }

    private javafx.scene.paint.Color toFX(java.awt.Color color) {
        return new javafx.scene.paint.Color(((double) color.getRed()) / 255,
                ((double) color.getGreen()) / 255,
        ((double) color.getBlue()) / 255,
        ((double) color.getAlpha()) / 255);
    }
}
