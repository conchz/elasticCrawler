package org.ec;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dolphineor on 2015-1-19.
 */
public class WorkerThreadPool {

    private final BlockingQueue<?> workers = new LinkedBlockingQueue<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();


    class WorkerThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(0);
        private final String namePrefix = "thread-crawler-worker-";

        public WorkerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }


        @Override
        public Thread newThread(Runnable r) {
            return new Thread(group, r, namePrefix + "[T#" + threadNumber.getAndIncrement() + "]", 0);
        }
    }

}