package org.spider.rx;

import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2015-10-12.
 *
 * @author dolphineor
 */
public class RxConcurrencyTest {

    public static void main(String[] args) {
        final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        RxConcurrencyTest rxConcurrencyTest = new RxConcurrencyTest();
        rxConcurrencyTest.schedule(Schedulers.from(executor), 10, false);
    }

    public void schedule(Scheduler scheduler, int numberOfSubTasks, boolean onTheSameWorker) {
        List<Integer> list = new ArrayList<>(0);
        AtomicInteger current = new AtomicInteger(0);
        Random random = new Random();
        Scheduler.Worker worker = scheduler.createWorker();

        Action0 addWork = () -> {
            synchronized (current) {
                System.out.println(" Add : " +
                        Thread.currentThread().getName() + " " + current.get());

                list.add(random.nextInt(current.get()));
                System.out.println(" End add : " +
                        Thread.currentThread().getName() + " " + current.get());
            }
        };

        Action0 removeWork = () -> {
            synchronized (current) {
                if (!list.isEmpty()) {
                    System.out.println(" Remove : " + Thread.currentThread().getName());
                    list.remove(0);
                    System.out.println(" End remove : " + Thread.currentThread().getName());
                }
            }
        };

        Action0 work = () -> {
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < numberOfSubTasks; i++) {
                current.set(i);
                System.out.println("Begin add!");

                if (onTheSameWorker) {
                    worker.schedule(addWork);
                } else {
                    scheduler.createWorker().schedule(addWork);
                }
                System.out.println("End add!");
            }

            while (!list.isEmpty()) {
                System.out.println("Begin remove!");

                if (onTheSameWorker) {
                    worker.schedule(removeWork);
                } else {
                    scheduler.createWorker().schedule(removeWork);
                }
                System.out.println("End remove!");
            }
        };
        worker.schedule(work);
    }
}
