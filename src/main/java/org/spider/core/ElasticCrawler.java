package org.spider.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.spider.scheduler.MemoryTaskQueue;
import org.spider.scheduler.Task;
import org.spider.scheduler.TaskQueue;
import rx.Observable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Created on 2015-01-18.
 *
 * @author dolphineor
 */
public class ElasticCrawler {

    /**
     * Spider configurations
     */
    public static Config config = ConfigFactory.defaultApplication();


    private final int scrapeThreadNum = config.getInt("spider.thread.tNum");

    private final ExecutorService executor = Executors.newFixedThreadPool(scrapeThreadNum);

    private final CountDownLatch latch = new CountDownLatch(scrapeThreadNum);

    private final TaskQueue taskQueue;


    protected ElasticCrawler(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }


    public ElasticCrawler addTask(List<Task> tasks) {
        tasks.stream().forEach(taskQueue::offer);
        return this;
    }

    public void runAsync() {
        executor.execute(() -> {
            final ScrapeWorker scrapeWorker = new ScrapeWorker();
            IntStream.range(0, scrapeThreadNum).forEach(i -> {
                executor.execute(() -> {
                    for (; ; )
                        Optional.ofNullable(taskQueue.take())
                                .ifPresent(task -> Observable.just(task).subscribe(scrapeWorker));
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
