package jp.gr.java_conf.alpius.pino.internal.filter.action;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.internal.layer.DrawableHelper;
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer;
import jp.gr.java_conf.alpius.pino.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.notification.Notification;
import jp.gr.java_conf.alpius.pino.notification.NotificationCenter;
import jp.gr.java_conf.alpius.pino.notification.NotificationType;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.Action;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class MeanBlur extends Action {
    public static final String ID = "pino:mean-blur";
    public static final String DESCRIPTION = "アクティブレイヤに平均化フィルタをかけます";

    public MeanBlur() {
        super(ID, DESCRIPTION);
    }

    @Override
    public void performed(ActionEvent e) {
        SelectionModel<LayerObject> selectionModel = ApplicationManager.getApp().getRoot().getLayer().getSelectionModel();
        if (selectionModel == null) {
            Notification notification = new Notification(
                    "対象のレイヤが不明です",
                    "平均化フィルタをかける対象のレイヤが不明です",
                    "",
                    NotificationType.WARN,
                    null
            );
            NotificationCenter.getInstance().notify(notification);
            return;
        }

        Objects.requireNonNull(selectionModel);
        LayerObject object = selectionModel.getSelectedItem();
        if (!(object instanceof DrawableLayer)) {
            Notification notification = new Notification(
                    "不正なレイヤ",
                    "このレイヤには平均化フィルタをかけられません。DrawableLayerを選択してください",
                    "",
                    NotificationType.WARN,
                    null
            );
            NotificationCenter.getInstance().notify(notification);
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

        TextField width = new TextField();
        TextFormatter<Integer> widthFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), 5
        );
        width.setTextFormatter(widthFormatter);
        TextField height = new TextField();
        TextFormatter<Integer> heightFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), 5);
        height.setTextFormatter(heightFormatter);

        dialog.getDialogPane().setContent(new VBox(new HBox(new Label("幅"), width), new HBox(new Label("高さ"), height)));

        dialog.showAndWait()
                .filter(it -> it == ButtonType.APPLY)
                .ifPresent(it -> {
                    DrawableLayer layer = (DrawableLayer) object;
                    WritableImage image = DrawableHelper.getImage(layer);
                    BufferedImage src = SwingFXUtils.fromFXImage(image, null);
                    jp.gr.java_conf.alpius.pino.filter.MeanBlur blur = new jp.gr.java_conf.alpius.pino.filter.MeanBlur();
                    blur.setWidth(widthFormatter.getValue());
                    blur.setHeight(heightFormatter.getValue());


                    BufferedImage res = blur.createFilter().filter(src, null);
                    image.getPixelWriter().setPixels(
                            0, 0, res.getWidth(), res.getHeight(), PixelFormat.getIntArgbInstance(),
                            res.getRGB(0, 0, res.getWidth(), res.getHeight(), null, 0, res.getWidth()),
                            0, res.getWidth()
                    );
                });



    }
}
