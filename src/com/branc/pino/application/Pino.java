package com.branc.pino.application;

import com.branc.pino.core.util.Disposable;
import com.branc.pino.core.util.Disposer;
import com.branc.pino.paint.brush.BrushManager;
import com.branc.pino.paint.layer.Drawable;
import com.branc.pino.paint.layer.LayerObject;
import com.branc.pino.project.ProjectManager;
import com.branc.pino.service.MutableServiceContainer;
import com.branc.pino.service.ServiceContainer;
import com.branc.pino.service.SimpleServiceContainer;
import com.branc.pino.ui.Root;
import com.branc.pino.ui.canvas.AutoRepaint;
import com.branc.pino.ui.canvas.DrawEventHandler;
import com.google.common.flogger.FluentLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.nio.file.Paths;
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
        s.setScene(scene);
        s.setTitle("Pino  ver 0.1.0");
        s.show();
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public EventDistributor getEventDistributor() {
        return distributor;
    }

    @Override
    public void stop() {
        Disposer.dispose(this);
        Disposer.dispose(lastDisposable);
    }

    @Override
    public void init() {
        appConfig = AppConfig.loadConfig();
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
