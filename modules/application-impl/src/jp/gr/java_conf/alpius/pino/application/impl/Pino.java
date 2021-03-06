/*
 * Copyright [2021] [shiro]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.gr.java_conf.alpius.pino.application.impl;

import com.google.common.flogger.FluentLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.PixelFormat;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jp.gr.java_conf.alpius.pino.application.util.AliasManager;
import jp.gr.java_conf.alpius.pino.application.util.IAliasManager;
import jp.gr.java_conf.alpius.pino.bootstrap.Main;
import jp.gr.java_conf.alpius.pino.disposable.Disposable;
import jp.gr.java_conf.alpius.pino.disposable.Disposer;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.gui.JFxWindow;
import jp.gr.java_conf.alpius.pino.gui.PinoRootContainer;
import jp.gr.java_conf.alpius.pino.gui.RootContainer;
import jp.gr.java_conf.alpius.pino.gui.screen.options.OptionScreen;
import jp.gr.java_conf.alpius.pino.gui.widget.MenuManager;
import jp.gr.java_conf.alpius.pino.history.History;
import jp.gr.java_conf.alpius.pino.notification.Publisher;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.service.MutableServiceContainer;
import jp.gr.java_conf.alpius.pino.service.SimpleServiceContainer;
import jp.gr.java_conf.alpius.pino.tool.ToolManager;
import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionEvent;
import jp.gr.java_conf.alpius.pino.ui.actionSystem.ActionUtils;
import jp.gr.java_conf.alpius.pino.util.ActiveModel;
import jp.gr.java_conf.alpius.pino.util.Key;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Pino extends Application implements jp.gr.java_conf.alpius.pino.application.Application {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private static Pino app;

    private final Disposable lastDisposable = Disposer.newDisposable("Application Internal Disposable");

    public Pino() {

        /* -- register core services -- */
        services.register(IAliasManager.class, AliasManager.create());
        services.register(BlendModeRegistry.class, new BlendModeRegistry());
        services.register(BrushManager.class, new BrushManager());
        services.register(GraphicManager.class, new GraphicManager());
        services.register(History.class, new HistoryImpl(100));
        services.register(MenuManager.class, new MenuManager());
        services.register(Publisher.class, new NotificationCenter());
        services.register(ProjectManager.class, initializeProjectMgr(new ProjectManagerImpl()));

    }

    private double fps = 30;

    private JFxWindow window;
    private final MutableServiceContainer services = new SimpleServiceContainer();
    private final Map<Object, Object> userData = new HashMap<>();
    private RepaintTimer timer;
    private EventDistributor eventDistributor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.atInfo().log();
        window = new JFxWindow(primaryStage);
        timer = new RepaintTimer(fps, this);
        RootContainer container = PinoRootContainer.load();

        var brsAM = BrushManager.getInstance().getActiveModel();
        brsAM.addListener(i -> container.getBrushEditor().setItem(brsAM.getActivatedItem()));
        container.getBrushEditor().setItem(brsAM.getActivatedItem());
        container.getBrushView().setItems((ObservableList<Brush>) BrushManager.getInstance().getBrushList());

        window.setRootContainer(container);
        Main.getStyle().ifPresentOrElse(style -> {
            window.getScene().getStylesheets().add(style);
            log.atInfo().log("stylesheet is set.");
        }, () -> log.atInfo().log("No stylesheet was found."));

        window.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::searchActionAndPerform);
        window.setTitle("Pino Paint");
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> exit());
        window.show();
        eventDistributor = new EventDistributor(window);
        services.register(ToolManager.class, eventDistributor);
        eventDistributor.activate(DrawTool.getInstance());
        Thread.currentThread().setUncaughtExceptionHandler(new ErrorHandler());
        services.register(OptionScreen.class, OptionScreen.create());
        eventDistributor.activate(eventDistributor.getActiveTool());
        eventDistributor.addListener((oldTool, newTool) -> container.getToolEditor().setItem(newTool));
        container.getToolEditor().setItem(eventDistributor.getActiveTool());

        log.atInfo().log("Application started at %s", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(Main.startUp));
    }

    private void searchActionAndPerform(KeyEvent e) {
        for (var binding: KeyBinding.getKeyConfigs()) {
            if (binding.getKey().match(e)) {
                ActionUtils.findByCanonicalName(binding.getValue())
                        .ifPresent(action -> action.performAction(new ActionEvent(e.getSource())));
            }
        }
    }

    @Override
    public void stop() {

    }

    public static Pino getApp() {
        return app;
    }

    @Override
    public void init() {
        app = this;
        log.atInfo().log("initialization completed");
    }

    @Override
    public JFxWindow getWindow() {
        return window;
    }

    @Override
    public void runLater(Runnable action) {
        Platform.runLater(action);
    }

    @Override
    public void runAndWait(Runnable action) throws InterruptedException {
        if (Platform.isFxApplicationThread()) {
            throw new Error("Can't invoke from FxApplicationThread.");
        }
        CountDownLatch latch = new CountDownLatch(1);
        runLater(() -> {
            action.run();
            latch.countDown();
        });
        latch.await();
    }

    @Override
    public void exit() {
        Disposer.dispose(this);
        log.atInfo().log("All children were disposed.");
        Platform.exit();
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return services.getService(serviceClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getUserData(Key<T> key) {
        return (T) userData.get(key);
    }

    @Override
    public <T> void putUserData(Key<T> key, T value) {
        userData.put(key, value);
    }

    private ProjectManager initializeProjectMgr(ProjectManager mgr) {
        mgr.addBeforeChange(project -> {
            timer.stop();
            initProject(project);
        });
        mgr.addOnChanged(project -> {
            updateCanvas(project);
            timer.start();
        });
        Disposer.registerDisposable(lastDisposable, mgr);
        return mgr;
    }

    public void setProjectAndDispose(Project project) {
        getService(ProjectManager.class).setAndDispose(project);
    }

    public Project getProject() {
        return getService(ProjectManager.class).get();
    }

    @SuppressWarnings("unchecked")
    private void initProject(Project project) {
        if (project == null) {
            window.getRootContainer()
                    .getLayerEditor()
                    .setItem(null);
            return;
        }

        var activeModel = project.getActiveModel();
        activeModel.addListener(index -> layerChanged(activeModel));

        window.getRootContainer()
                .getLayerEditor()
                .setItem(activeModel.getActivatedItem());
        window.getRootContainer()
                .getLayerView()
                .setItems((ObservableList<LayerObject>) project.getChildren());  // PinoProject???getChildren()?????????List???ObservableList??????
    }

    private void layerChanged(ActiveModel<LayerObject> activeModel) {
        window.getRootContainer()
                .getLayerEditor()
                .setItem(activeModel.getActivatedItem());
    }

    @Override
    public void dispose() {
        timer.stop();
        eventDistributor.dispose();
        Disposer.dispose(lastDisposable);
    }

    private void updateCanvas(Project project) {
        var container = window.getRootContainer();
        var canvas = container.getCanvas();
        canvas.setScaleX(1);
        canvas.setScaleY(1);
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        canvas.setRotate(0);
        if (project == null) {
            canvas.setWidth(0);
            canvas.setHeight(0);
        } else {
            canvas.setWidth(project.getWidth());
            canvas.setHeight(project.getHeight());
        }
    }


    /**
     * Canvas???????????????????????????
     */
    void repaint() {
        var project = getService(ProjectManager.class).get();
        var canvas = project.getCanvas();
        var layers = project.getChildren();
        var g = canvas.createGraphics();
        var aoi = new Rectangle(canvas.getWidth(), canvas.getHeight());
        g.setComposite(AlphaComposite.Src);
        g.setPaint(canvas.getBackground());
        g.fillRect(0, 0, aoi.width, aoi.height);

        ListIterator<LayerObject> itr = layers.listIterator(layers.size());
        while (itr.hasPrevious()) {
            itr.previous().render(g, aoi, false);
        }
        g.dispose();

        var context = window.getRootContainer().getCanvas().getGraphicsContext2D();
        context.getPixelWriter()
                .setPixels(0, 0, canvas.getWidth(), canvas.getHeight(), PixelFormat.getIntArgbInstance(),
                        canvas.snapshot().getRGB(0, 0, canvas.getWidth(), canvas.getHeight(), null, 0, canvas.getWidth()),
                        0, canvas.getWidth());
    }


    private static class RepaintTimer {
        private double fps;
        private final Pino app;
        private ScheduledExecutorService service;

        public RepaintTimer(double fps, Pino app) {
            this.fps = fps;
            this.app = app;
        }

        public void setFps(double fps, boolean restart) {
            this.fps = fps;
            if (restart) {
                stop();
                start();
            }
        }

        public double getFps() {
            return fps;
        }

        public void start() {
            service = Executors.newScheduledThreadPool(1);
            service.scheduleAtFixedRate(app::repaint, 0, computeMilliSec(fps), TimeUnit.MILLISECONDS);
        }

        public void stop() {
            if (service != null) {
                service.shutdown();
            }
        }

        private static long computeMilliSec(double fps) {
            return (long) (1000 / fps + 0.5);
        }
    }
}
