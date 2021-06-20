package jp.gr.java_conf.alpius.pino.internal.application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.gr.java_conf.alpius.commons.base.Validate;
import jp.gr.java_conf.alpius.pino.application.Pino;
import jp.gr.java_conf.alpius.pino.internal.util.Utils;

public final class ApplicationHelper {
    private static ApplicationAccessor accessor;

    static {
        Utils.forceInit(Pino.class);
    }

    public static void setAccessor(ApplicationAccessor newAccessor) {
        Validate.check(accessor == null);
        accessor = newAccessor;
    }

    public static ApplicationAccessor getAccessor() {
        Validate.check(accessor != null);
        return accessor;
    }

    public static Stage getStage(Pino app) {
        return getAccessor().getStage(app);
    }

    public static Scene getScene(Pino app) {
        return getAccessor().getScene(app);
    }

    public interface ApplicationAccessor {
        void setHelper(Pino app, ApplicationHelper helper);
        ApplicationHelper getHelper(Pino pino);
        Stage getStage(Pino app);
        Scene getScene(Pino app);
    }
}
