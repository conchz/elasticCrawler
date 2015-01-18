package org.elasticcrawler.core;

import org.elasticcrawler.downloader.Downloader;
import org.elasticcrawler.extractor.Extractor;
import org.elasticcrawler.extractor.HtmlExtractor;
import org.elasticcrawler.extractor.Page;
import org.elasticcrawler.scheduler.TaskQueue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class TaskMaster implements Runnable {

    private final StampedLock lock = new StampedLock();

    private TaskQueue taskQueue = new TaskQueue();

    private ConcurrentHashMap<String, Downloader> downloaderMap = new ConcurrentHashMap<>();

    private Extractor extractor = new HtmlExtractor();

    private int threadNum = 1;

    private boolean isAsync;


    @Override
    public void run() {
        init();

        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        executorService.execute(() -> {
            for (; ; ) {
                String result = processRequest();
                if (result == null)
                    break;

                extractor.extract(new Page(result));
            }
        });

    }

    public TaskMaster setAsync() {
        this.isAsync = true;
        return this;
    }

    public TaskMaster addTask(List<Task> tasks) {
        long stamp = lock.writeLock();
        try {
            tasks.stream().forEach(taskQueue::offer);
        } finally {
            lock.unlockWrite(stamp);
        }

        return this;
    }

    private void init() {
    }

    private String processRequest() {
        Task task = taskQueue.take();

        if (task != null) {
            try {
                Downloader downloader = downloaderMap.putIfAbsent(task.getDownloader().getName(), task.getDownloader());
                return downloader.download(task);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 暂时停止爬取
            sleep(10);
        }

        return null;
    }

    public TaskMaster setThread(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public static TaskMaster build() {
        return new TaskMaster();
    }

    private void sleep(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
