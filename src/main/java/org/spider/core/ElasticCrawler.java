package org.spider.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.spider.scheduler.MemoryTaskQueue;
import org.spider.scheduler.Task;
import org.spider.scheduler.TaskQueue;
import org.spider.util.Logs;
import rx.Observable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created on 2015-01-18.
 *
 * @author dolphineor
 */
public class ElasticCrawler extends Logs implements Runnable {

    public static Config config;


    protected static final int STATUS_INIT = 0;

    protected static final int STATUS_RUNNING = 1;

    protected static final int STATUS_STOPPED = -1;

    protected final AtomicInteger status;

    protected final int scrapeThreadNum;

    protected final ExecutorService executor;

    protected final CountDownLatch latch;

    protected final boolean exitWhenComplete;

    protected final TaskQueue taskQueue;


    protected ElasticCrawler(TaskQueue taskQueue) {
        logger.info("Initializing Spider...");
        loadConfig();

        // Initialize Spider
        this.taskQueue = taskQueue;
        this.scrapeThreadNum = config.getInt("spider.threadNum");
        this.executor = Executors.newFixedThreadPool(scrapeThreadNum);
        this.latch = new CountDownLatch(scrapeThreadNum);
        this.status = new AtomicInteger(STATUS_INIT);
        this.exitWhenComplete = config.getBoolean("spider.exitWhenComplete");
    }


    public ElasticCrawler addTask(List<Task> tasks) {
        tasks.forEach(taskQueue::offer);
        return this;
    }

    public Status getStatus() {
        return Status.fromValue(status.get());
    }

    @Override
    public void run() {
        while (true) {
            int nowStatus = status.get();
            if (nowStatus == STATUS_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }

            if (status.compareAndSet(nowStatus, STATUS_RUNNING)) {
                break;
            }
        }
        logger.info("Spider start succeeded!");

        executor.execute(() -> {
            final ScrapeWorker scrapeWorker = new ScrapeWorker();
            IntStream.range(0, scrapeThreadNum).forEach(i -> {
                executor.execute(() -> {
                    while (status.get() == STATUS_RUNNING) {
                        Optional<Task> taskOptional = Optional.ofNullable(taskQueue.take());
                        if (taskOptional.isPresent()) {
                            Observable.just(taskOptional.get()).subscribe(scrapeWorker);
                        } else {
                            if (isExitWhenComplete()) {
                                stop();
                                break;
                            }
                        }

                    }
                });

                latch.countDown();
            });
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected enum Status {
        INIT(0), RUNNING(1), STOPPED(-1);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        int value() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.value() == value) {
                    return status;
                }
            }

            // default value
            return INIT;
        }
    }

    public boolean isExitWhenComplete() {
        return exitWhenComplete;
    }

    protected void loadConfig() {
        if (Objects.isNull(config)) {
            synchronized (this) {
                if (Objects.isNull(config)) {
                    config = ConfigFactory.defaultApplication();
                }
            }
        }
    }

    public void runAsync() {
        Thread thread = new Thread(this, "Thread-ElasticCrawler-Main");
        thread.setDaemon(false);
        thread.start();
    }

    public void start() {
        runAsync();
    }

    public void stop() {
        if (executor.isShutdown()) {
            throw new IllegalStateException("Spider has already stopped!");
        } else {
            executor.shutdown();
        }

        if (status.compareAndSet(STATUS_RUNNING, STATUS_STOPPED)) {
            logger.info("Spider stop succeeded!");
        } else {
            logger.info("Spider stop failed!");
        }
    }


    public static ElasticCrawler create() {
        return new ElasticCrawler(new MemoryTaskQueue());
    }

    public static ElasticCrawler create(TaskQueue taskQueue) {
        return new ElasticCrawler(taskQueue);
    }

    public static ElasticCrawler create(Config config) {
        ElasticCrawler.config = config;
        return new ElasticCrawler(new MemoryTaskQueue());
    }

    public static ElasticCrawler create(Config config, TaskQueue taskQueue) {
        ElasticCrawler.config = config;
        return new ElasticCrawler(taskQueue);
    }
}
