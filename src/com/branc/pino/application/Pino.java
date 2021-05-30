package com.branc.pino.application;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.notification.Notification;
import com.branc.pino.notification.NotificationCenter;
import com.branc.pino.notification.NotificationType;
import com.branc.pino.notification.Publisher;
import com.branc.pino.project.ProjectManager;
import com.branc.pino.project.ProjectRenderer;
import com.branc.pino.service.MutableServiceContainer;
import com.branc.pino.service.ServiceContainer;
import com.branc.pino.service.SimpleServiceContainer;
import com.branc.pino.ui.KeyMap;
import com.branc.pino.ui.Root;
import com.branc.pino.ui.actionSystem.ActionEvent;
import com.branc.pino.ui.actionSystem.ActionNotFoundException;
import com.branc.pino.ui.actionSystem.ActionRegistry;
import com.branc.pino.ui.canvas.AutoRepaint;
import com.google.common.flogger.FluentLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class Pino extends Application implements Disposable, ServiceContainer {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    public static void main(String[] args) {
        launch(args);
    }

    private final Disposable lastDisposable = Disposer.newDisposable();

    private Root root;
    private final MutableServiceContainer serviceContainer = new SimpleServiceContainer();
    private AppConfig appConfig;
    private AutoRepaint autoRepaint;
    private EventDistributor distributor;

    @Override
    public void start(Stage s) throws Exception {
        FXMLLoader loader;
        if (appConfig.getRootFXML().equals(AppConfig.ROOT_DEFAULT)) {
            loader = Root.load();
        } else {
            loader = Root.load(Paths.get(appConfig.getRootFXML()));
        }
        root = loader.getController();
        autoRepaint = AutoRepaint.create(root.getCanvas(), () -> ProjectRenderer.render(ProjectManager.getInstance().getProject(), ProjectRenderer.RenderingOption.IGNORE_INVISIBLE));

        distributor = new EventDistributor();
        autoRepaint.setFps(appConfig.getCanvasFps());
        Disposer.registerDisposable(lastDisposable, root);
        Disposer.registerDisposable(lastDisposable, autoRepaint);
        Disposer.registerDisposable(lastDisposable, distributor);
        autoRepaint.start();
        double w = appConfig.getStageWidth();
        double h = appConfig.getStageHeight();
        Scene scene;
        if (w == AppConfig.USE_PREF_SIZE || h == AppConfig.USE_PREF_SIZE) {
            scene = new Scene(loader.getRoot());
        } else if (w == AppConfig.FULL_SCREEN || h == AppConfig.FULL_SCREEN) {
            scene = new Scene(loader.getRoot());
            s.setFullScreen(true);
        } else if (w == AppConfig.COMPUTE_MAX_SIZE || h == AppConfig.COMPUTE_MAX_SIZE) {
            scene = new Scene(loader.getRoot());
            s.setMaximized(true);
        } else {
            scene = new Scene(loader.getRoot(), w, h);
        }
        scene.setOnKeyPressed(this::searchAndPerform);
        scene.setOnKeyTyped(this::searchAndPerform);
        scene.getStylesheets().add(Paths.get(System.getProperty("appDir", "."), "data", "style.css").toUri().toURL().toExternalForm());
        s.setScene(scene);
        s.setTitle("Pino  ver 0.2.0");
        s.show();
    }

    private void searchAndPerform(KeyEvent e) {
        getService(KeyMap.class).searchFor(e).ifPresent(it -> {
            try {
                ActionRegistry.getInstance().find(it).performed(new ActionEvent(e.getSource()));
            } catch (ActionNotFoundException ex) {
                Notification notification = new Notification(
                        "アクションが見つかりませんでした",
                        String.format("アクション[id=%s]は見つかりませんでした。", it),
                        "",
                        NotificationType.ERROR,
                        Collections.emptyList()
                );
                getService(NotificationCenter.class).notify(notification);
            }
        });
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public EventDistributor getEventDistributor() {
        return distributor;
    }

    @Override
    public void stop() {
        try {
            appConfig.save();
            KeyMapWriter keyMapWriter = new KeyMapWriter();
            keyMapWriter.store(getService(KeyMap.class), Paths.get(System.getProperty("appDir", "."), "data", "KeyMap.xml"));
        } catch (IOException ioException) {
            StringWriter out = new StringWriter();
            PrintWriter writer = new PrintWriter(out);
            ioException.printStackTrace(writer);
            Notification notification = new Notification(
                    "エラー",
                    "設定の保存時にエラーが発生しました。",
                    out.getBuffer().toString(),
                    NotificationType.ERROR,
                    null
            );
            getService(NotificationCenter.class).notify(notification);
        }
        Disposer.dispose(this);
        Disposer.dispose(lastDisposable);
    }

    @Override
    public void init() {
        appConfig = AppConfig.loadConfig(System.getProperty("appDir", "."));
        ApplicationManager.setApplication(this);
        CoreServices.load(serviceContainer);
    }

    public boolean isApplicationThread() {
        return Platform.isFxApplicationThread();
    }

    public void runLater(Runnable action) {
        Platform.runLater(action);
    }

    public void runAndWait(Runnable action) {
        if (isApplicationThread()) {
            throw new Error("Can't call from Application Thread");
        }
        CountDownLatch latch = new CountDownLatch(1);
        runLater(() -> {
            action.run();
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit() {
        Platform.exit();
    }

    /**
     * 画面に表示されるRootを返します。アプリケーションが開始される前に呼び出されるとnullが返されます
     * @return A Root instance or null
     */
    public Root getRoot() {
        return root;
    }

    @Override
    public void dispose() {

    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return serviceContainer.getService(serviceClass);
    }
}
