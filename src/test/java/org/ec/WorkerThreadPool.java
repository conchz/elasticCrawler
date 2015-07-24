package org.ec;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dolphineor on 2015-1-19.
 */
public class WorkerThreadPool {

    private final Queue<Object> workers = new ConcurrentLinkedQueue<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();


    class WorkerThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix = "crawl-worker-";

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