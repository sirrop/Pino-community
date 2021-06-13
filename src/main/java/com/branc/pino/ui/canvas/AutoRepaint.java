package com.branc.pino.ui.canvas;

import com.branc.pino.core.util.Disposable;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.function.Supplier;

public interface AutoRepaint extends Disposable {
    void start();

    void pause();

    void resume();

    void shutdown();

    void setFps(double rate);

    double getFps();

    // sourceは再描画が不要なときはひとつ前に返したものと同じWritableImageを返すようにすると処理をスキップできます
    static AutoRepaint create(Canvas target, Supplier<Image> source) {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Canvas.class).toInstance(target);
                bind(AutoRepaintImpl.ImageSupplier.class).toInstance(source::get);
                bind(SchedulerFactory.class).toInstance(new StdSchedulerFactory());
                bind(AutoRepaintImpl.TaskFactory.class);
                bind(AutoRepaintImpl.QuartzEx.class).to(AutoRepaintImpl.QuartzExImpl.class);
            }
        });
        return injector.getInstance(AutoRepaintImpl.class);
    }
}
