package jp.gr.java_conf.alpius.pino.internal.ui.canvas;

import com.google.common.flogger.FluentLogger;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import jp.gr.java_conf.alpius.pino.application.ApplicationError;
import jp.gr.java_conf.alpius.pino.application.ApplicationManager;
import jp.gr.java_conf.alpius.pino.core.annotaion.Internal;
import jp.gr.java_conf.alpius.pino.project.Project;
import jp.gr.java_conf.alpius.pino.project.ProjectManager;
import jp.gr.java_conf.alpius.pino.ui.canvas.AutoRepaint;
import jp.gr.java_conf.alpius.sat.utils.Key;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.util.function.Supplier;

@Internal
public class AutoRepaintImpl implements AutoRepaint {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    public static final Key<Boolean> DIRTY_FLAG = Key.get();
    private final JobDetail job;
    private Trigger trigger;
    private final Scheduler scheduler;

    @Inject
    public AutoRepaintImpl(QuartzEx quartz) {
        job = JobBuilder.newJob(Task.class)
                .withIdentity("AutoRepaintJob", "Canvas")
                .build();
        trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withIdentity("AutoRepaintTrigger", "Canvas")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMilliseconds(100L)
                                .repeatForever()
                ).build();

        scheduler = quartz.getScheduler();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new ApplicationError(e);
        }
    }

    @Override
    public void dispose() {
        shutdown();
    }

    @Override
    public void start() {
        try {
            log.atInfo().log("start process");
            scheduler.start();
        } catch (SchedulerException e) {
            throw new ApplicationError(e);
        }
    }

    @Override
    public void pause() {
        try {
            log.atInfo().log("pause process");
            scheduler.pauseJob(job.getKey());
        } catch (SchedulerException e) {
            throw new ApplicationError(e);
        }
    }

    @Override
    public void resume() {
        try {
            log.atInfo().log("resume process");
            scheduler.resumeJob(job.getKey());
        } catch (SchedulerException e) {
            throw new ApplicationError(e);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.atInfo().log("shutdown process");
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new ApplicationError(e);
        }
    }

    private double fps = 10;

    @Override
    public void setFps(double rate) {
        long time = (long) (1000 / rate);
        if (time <= 0) {
            throw new IllegalArgumentException();
        }

        var newTrigger = TriggerBuilder.newTrigger()
                .startNow()
                .withIdentity("AutoRepaintTrigger", "Canvas")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMilliseconds(time)
                                .repeatForever()
                )
                .build();
        try {
            boolean running = scheduler.isStarted();
            if (running) scheduler.pauseAll();
            fps = rate;
            scheduler.rescheduleJob(trigger.getKey(), newTrigger);
            trigger = newTrigger;
            if (running) scheduler.resumeAll();
        } catch (SchedulerException e) {
            throw new ApplicationError(e);
        }
    }

    @Override
    public double getFps() {
        return fps;
    }

    @Singleton
    public static class Task implements Job {
        @Inject
        private Canvas target;

        @Inject
        private ImageSupplier source;

        private Image old;

        @Override
        public void execute(JobExecutionContext context) {
            Project project = ProjectManager.getInstance().getProject();
            if (project == null) {
                return;
            }

            Boolean isDirty = project.getUserDataHolder().getUserData(DIRTY_FLAG);
            if (isDirty != Boolean.TRUE) {
                return;
            }

            var newImage = source.get();
            if (old != newImage) {
                var g = target.getGraphicsContext2D();
                Platform.runLater(() -> {
                    g.clearRect(0, 0, target.getWidth(), target.getHeight());
                    old = newImage;
                    g.drawImage(newImage, 0, 0);
                    ApplicationManager.getApp()
                            .getRoot()
                            .getLayer()
                            .refresh();
                });
            }

        }
    }

    public static class TaskFactory implements JobFactory {

        @Inject
        private Injector injector;

        @Override
        public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) {
            JobDetail job = bundle.getJobDetail();
            return injector.getInstance(job.getJobClass());
        }
    }

    public interface QuartzEx {
        Scheduler getScheduler();
    }

    @Singleton
    public static class QuartzExImpl implements QuartzEx {

        private final Scheduler scheduler;

        @Inject
        public QuartzExImpl(SchedulerFactory schedulerFactory, TaskFactory taskFactory) throws SchedulerException {
            this.scheduler = schedulerFactory.getScheduler();
            this.scheduler.setJobFactory(taskFactory);
            scheduler.start();
        }

        @Override
        public final Scheduler getScheduler() {
            return scheduler;
        }
    }

    public interface ImageSupplier extends Supplier<Image> {
    }
}
