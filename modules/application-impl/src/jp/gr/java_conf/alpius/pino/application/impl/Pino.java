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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.PixelFormat;
import javafx.stage.Stage;
import jp.gr.java_conf.alpius.pino.graphics.brush.Brush;
import jp.gr.java_conf.alpius.pino.graphics.layer.LayerObject;
import jp.gr.java_conf.alpius.pino.history.History;
import jp.gr.java_conf.alpius.pino.notification.Publisher;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.service.MutableServiceContainer;
import jp.gr.java_conf.alpius.pino.service.SimpleServiceContainer;
import jp.gr.java_conf.alpius.pino.tool.ToolManager;
import jp.gr.java_conf.alpius.pino.tool.plugin.DrawTool;
import jp.gr.java_conf.alpius.pino.util.ActiveModel;
import jp.gr.java_conf.alpius.pino.util.Key;
import jp.gr.java_conf.alpius.pino.window.Window;
import jp.gr.java_conf.alpius.pino.window.impl.JFxWindow;
import jp.gr.java_conf.alpius.pino.window.impl.MenuManager;
import jp.gr.java_conf.alpius.pino.window.impl.PinoRootContainer;
import jp.gr.java_conf.alpius.pino.window.impl.RootContainer;

import java.awt.*;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static jp.gr.java_conf.alpius.pino.internal.InternalLogger.log;

public class Pino extends Application implements jp.gr.java_conf.alpius.pino.application.Application {
    private static Pino app;

    public Pino() {

        /* -- register core services -- */
        services.register(BrushManager.class, new BrushManager());
        services.register(GraphicManager.class, new GraphicManager());
        services.register(History.class, new HistoryImpl(100));
        services.register(MenuManager.class, new MenuManager());
        services.register(Publisher.class, new NotificationCenter());

    }

    private double fps = 30;

    private JFxWindow window;
    private final MutableServiceContainer services = new SimpleServiceContainer();
    private final Map<Object, Object> userData = new HashMap<>();
    private Project project;
    private RepaintTimer timer;
    private EventDistributor eventDistributor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = new JFxWindow(primaryStage);
        timer = new RepaintTimer(fps, this);
        RootContainer container = PinoRootContainer.load();

        var brsAM = BrushManager.getInstance().getActiveModel();
        brsAM.addListener(i -> container.getBrushEditor().setItem(brsAM.getActivatedItem()));
        container.getBrushEditor().setItem(brsAM.getActivatedItem());
        container.getBrushView().setItems((ObservableList<Brush>) BrushManager.getInstance().getBrushList());

        window.setRootContainer(container);
        window.setTitle("Pino Paint");
        window.show();
        eventDistributor = new EventDistributor(window);
        services.register(ToolManager.class, eventDistributor);
        eventDistributor.activate(DrawTool.getInstance());
    }

    @Override
    public void stop() {
        timer.stop();
        eventDistributor.dispose();
    }

    public static Pino getApp() {
        return app;
    }

    @Override
    public void init() {
        app = this;
    }

    @Override
    public Window getWindow() {
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

    public void setProjectAndDispose(Project project) {
        timer.stop();
        initProject(project);
        var old = this.project;
        this.project = project;
        if (old != null) {
            old.dispose();
        }
        updateCanvas();
        timer.start();
    }

    /**
     * 古いProjectの破棄は、呼び出し元の役割です。
     * @param project project
     */
    public void setProject(Project project) {
        timer.stop();
        initProject(project);
        this.project = project;
        updateCanvas();
        timer.start();
    }

    public Project getProject() {
        return project;
    }

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
                .setItems((ObservableList<LayerObject>) project.getLayers());
    }

    private void layerChanged(ActiveModel<LayerObject> activeModel) {
        window.getRootContainer()
                .getLayerEditor()
                .setItem(activeModel.getActivatedItem());
    }

    @Override
    public void dispose() {
        if (project != null) {
            project.dispose();
        }
    }

    private void updateCanvas() {
        var container = window.getRootContainer();
        var canvas = container.getCanvas();
        if (project == null) {
            canvas.setWidth(0);
            canvas.setHeight(0);
            log("canvas has been updated! [width: %d, height: %d]", 0, 0);
        } else {
            canvas.setWidth(project.getWidth());
            canvas.setHeight(project.getHeight());
            log("canvas has been updated! [width: %d, height: %d]", project.getWidth(), project.getHeight());
        }
    }


    /**
     * Canvasの再描画を行います
     */
    void repaint() {
        var canvas = project.getCanvas();
        var layers = project.getLayers();
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