package org.ec;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class ConcurrentLinkedQueueTest {
    private static int taskNum = 2;   // 任务数量
    private static CountDownLatch latch = new CountDownLatch(taskNum);
    private static Queue<Integer> queue = new ConcurrentLinkedQueue<>();

    public static void init() {
        int count = 1_000_000;
        for (int i = 0; i < count; i++) {
            queue.offer(i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedQueueTest.init();
        ExecutorService service = Executors.newFixedThreadPool(4);
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < taskNum; i++) {
            service.submit(() -> {
                while (!queue.isEmpty()) {
                    queue.poll();
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("cost time: " + (System.currentTimeMillis() - timeStart) + "ms");
        service.shutdown();
    }

}
