package org.elasticcrawler;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dolphineor on 2015-1-19.
 */
public class WorkerThreadPool {

    private final ConcurrentLinkedQueue<Object> workers = new ConcurrentLinkedQueue<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void v(){
        ExecutorService executorService = Executors.newCachedThreadPool();

    }

    static final class WorkerThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix = "crawl-worker";

        public WorkerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
        }


        @Override
        public Thread newThread(Runnable r) {
            return new Thread(group, r, namePrefix + "[T#" + threadNumber.getAndIncrement() + "]", 0);
        }
    }

}