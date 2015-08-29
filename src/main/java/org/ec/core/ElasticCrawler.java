package org.ec.core;

import org.ec.downloader.HttpClientDownloader;
import org.ec.extractor.HtmlExtractor;
import org.ec.scheduler.MemoryTaskQueue;
import org.ec.scheduler.Task;
import org.ec.scheduler.TaskQueue;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import rx.Observable;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author dolphineor
 */
public class ElasticCrawler {

    public static final Config CONFIG = ConfigFactory.defaultApplication();
    public static final String SCRAPE_URL = "http://search.jd.com/Search?keyword=%s&enc=utf-8";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String[] arr = {"冬装", "毛衣", "羽绒服", "书包", "手套", "夹克", "卫衣", "暖宝宝", "围巾"};
        List<Task> tasks = new ArrayList<>();

        for (String k : arr) {
            Task task = new Task();
            task.setCharset("GBK");
            k = java.net.URLEncoder.encode(k, "UTF-8");
//            task.setUrl("http://s.taobao.com/search?q=" + k);
            task.setUrl(String.format(SCRAPE_URL, k));
            task.setDownloader(HttpClientDownloader.create());
            task.setExtractor(HtmlExtractor.create());
            tasks.add(task);
        }


        // start a elasticCrawler
//        TaskMaster taskMaster = TaskMaster.build();
//        taskMaster.setWorkerThread(2).schedule(tasks);
//        taskMaster.start();
//        taskMaster.shutdown();
        ElasticCrawler crawler = ElasticCrawler.create().addTask(tasks);
        crawler.runAsync();

    }

    private final int scrapeThreadNum = CONFIG.getInt("elasticCrawler.thread.tNum");

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
            ScrapeWorker scrapeWorker = new ScrapeWorker();
            for (int i = 0; i < scrapeThreadNum; i++) {
                executor.execute(() -> {
                    while (true) {
                        Task task = taskQueue.take();
                        if (task != null) {
                            Observable<Task> taskObservable = Observable.just(task);
                            taskObservable.subscribe(scrapeWorker);
                        }
                    }
                });

                latch.countDown();
            }
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
}
