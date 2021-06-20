package jp.gr.java_conf.alpius.pino.ui.editor.skin;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;
import jp.gr.java_conf.alpius.pino.annotations.Bind;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.core.util.Disposable;
import jp.gr.java_conf.alpius.pino.graphics.BlendMode;
import jp.gr.java_conf.alpius.pino.internal.beans.PropertyUtils;
import jp.gr.java_conf.alpius.pino.internal.util.Utils;
import jp.gr.java_conf.alpius.pino.ui.attr.NumberAttribute;
import jp.gr.java_conf.alpius.pino.ui.attr.StringAttribute;
import jp.gr.java_conf.alpius.pino.ui.attr.ViewType;
import jp.gr.java_conf.alpius.pino.ui.editor.EditorTarget;
import jp.gr.java_conf.alpius.pino.ui.editor.InvocationException;

import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.*;

@Internal
public final class PropertyEditorBuilder {

    public ViewType getDefaultViewType(Class<?> type) throws NoDefaultViewTypeException{
        if (type == int.class || type == Integer.class || type == long.class || type == Long.class || type == float.class || type == Float.class || type == double.class || type == Double.class)
            return ViewType.SLIDER;
        if (type == String.class) return ViewType.TEXTAREA;
        if (type == boolean.class || type == Boolean.class) return ViewType.CHECK_BOX;
        if (type == Color.class) return ViewType.COLOR_CHOOSER;
        if (type.getSuperclass() == Enum.class) return ViewType.COMBO_BOX;
        throw new NoDefaultViewTypeException(String.format("There is no default type for %s.", type));
    }

    public Node createPropertyEditor(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        ViewType type;
        try {
            type = PropertyUtils.getValueElse(desc, ViewType.KEY, ViewType::valueOf, getDefaultViewType(desc.getPropertyType()));
        } catch (NoDefaultViewTypeException e) {
            throw new RuntimeException(String.format("Can't create view for %s", desc));
        }

        switch (type) {
            case SLIDER:
                return slider(desc, target, parent);
            case TEXTAREA:
                return textarea(desc, target, parent);
            case COMBO_BOX:
                return comboBox(desc, target, parent);
            case CHECK_BOX:
                return checkBox(desc, target, parent);
            case COLOR_CHOOSER:
                return colorChooser(desc, target, parent);
            default:
                throw new IllegalArgumentException("Can't create property editor.");
        }
    }

    private Node slider(PropertyDescriptor property, EditorTarget target, Disposable parent) {
        Label label = new Label(property.getDisplayName());
        Slider slider = new Slider();

        double min = PropertyUtils.getDoubleElse(property, NumberAttribute.MIN, -2000);
        double max = PropertyUtils.getDoubleElse(property, NumberAttribute.MAX, 2000);
        double blockIncrement = PropertyUtils.getDoubleElse(property, NumberAttribute.BLOCK_INCREMENT, 1);

        slider.setMax(max);
        slider.setMin(min);
        slider.setBlockIncrement(blockIncrement);

        TextField textField = new TextField();
        TextFormatter<Number> formatter;
        formatter = new TextFormatter<>(new NumberStringConverter(), PropertyUtils.get(property, target));
        textField.setTextFormatter(formatter);
        slider.valueProperty().bindBidirectional(formatter.valueProperty());

        slider.valueProperty().addListener((obs, old, newValue) -> {
            var type = property.getPropertyType();
            if (type == int.class || type == Integer.class) {
                PropertyUtils.set(property, target, newValue.intValue());
            } else if (type == long.class || type == Long.class) {
                PropertyUtils.set(property, target, newValue.longValue());
            } else if (type == float.class || type == Float.class) {
                PropertyUtils.set(property, target, newValue.floatValue());
            } else if (type == double.class || type == Double.class) {
                PropertyUtils.set(property, target, newValue.doubleValue());
            } else {
                throw new IllegalStateException(String.format("%s is unknown type.", type));
            }
        });

        return new HBox(label, slider, textField);
    }

    private Node textarea(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        Label label = new Label(desc.getDisplayName());
        String regexp = PropertyUtils.getStringElse(desc, StringAttribute.REGEXP, ".*");
        TextField field = new TextField();
        field.setText(PropertyUtils.get(desc, target));
        TextFormatter<String> formatter = new TextFormatter<>(c -> {
            if (c.getControlNewText().matches(regexp)) {
                return c;
            } else {
                return null;
            }
        });
        field.setTextFormatter(formatter);
        formatter.valueProperty().addListener((obs, old, newValue) -> {
            Class<?> type = desc.getPropertyType();
            Optional<Exception> result;
            if (type == int.class || type == Integer.class) {
                PropertyUtils.set(desc, target, Integer.parseInt(newValue));
            } else if (type == long.class || type == Long.class) {
                PropertyUtils.set(desc, target, Long.parseLong(newValue));
            } else if (type == float.class || type == Float.class) {
                PropertyUtils.set(desc, target, Float.parseFloat(newValue));
            } else if (type == double.class || type == Double.class) {
                PropertyUtils.set(desc, target, Double.parseDouble(newValue));
            } else if (type == String.class) {
                PropertyUtils.set(desc, target, newValue);
            } else {
                try {
                    Method valueOf = type.getMethod("valueOf", String.class);
                    PropertyUtils.set(desc, target, valueOf.invoke(null, newValue));
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new InvocationException(e);
                }
            }
        });
        return new HBox(label, field);
    }

    private Node comboBox(PropertyDescriptor desc, EditorTarget target, Disposable parent) {
        Label label = new Label(desc.getDisplayName());
        assert desc.getPropertyType() == BlendMode.class;

        List<String> names = new ArrayList<>();
        Map<String, String> nameMap = new HashMap<>();

        Field[] fields = desc.getPropertyType().getDeclaredFields();

        ResourceBundle bundle = Utils.findBundle(desc.getPropertyType());

        for (Field field: fields) {
            var ann = field.getAnnotation(Bind.class);
            if (ann == null) continue;

            if (bundle == null) {
                names.add(desc.getDisplayName());
            } else {
                String id;
                if (ann.id().isEmpty() || ann.id().isBlank()) {
                    id = field.getName() + ".name";
                } else {
                    id = ann.id();
                }
                String name = bundle.getString(id);
                names.add(name);
                nameMap.put(name, field.getName());
            }
        }

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().setAll(names);
        comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> PropertyUtils.set(desc, target, BlendMode.valueOf(nameMap.get(newValue))));
        Platform.runLater(() -> comboBox.getSelectionModel().selectFirst());

        return new HBox(label, comboBox);
    }

    private Node checkBox(PropertyDescriptor property, EditorTarget target, Disposable parent) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(PropertyUtils.get(property, target));
        checkBox.setText(property.getDisplayName());
        checkBox.selectedProperty().addListener((obs, old, newValue) -> PropertyUtils.set(property, target, newValue));
        return checkBox;
    }

    private Node colorChooser(PropertyDescriptor property, EditorTarget target, Disposable parent) {
        Label label = new Label(property.getDisplayName());
        ColorPicker picker = new ColorPicker();
        picker.valueProperty().addListener((obs, old, newValue) -> PropertyUtils.set(property, target, toAWT(newValue)));
        picker.setValue(toFX(PropertyUtils.get(property, target)));
        return new HBox(label, picker);
    }

    private Color toAWT(javafx.scene.paint.Color color) {
        return new Color((float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue(),
                (float) color.getOpacity());
    }

    private javafx.scene.paint.Color toFX(Color color) {
        return new javafx.scene.paint.Color(((double) color.getRed()) / 255,
                ((double) color.getGreen()) / 255,
                ((double) color.getBlue()) / 255,
                ((double) color.getAlpha()) / 255);
    }

    private static class NoDefaultViewTypeException extends Exception {
        NoDefaultViewTypeException(String mes) {
            super(mes);
        }
    }
}
