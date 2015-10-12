package org.spider.rx;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2015-10-12.
 *
 * @author dolphineor
 */
public class ObservableListTest {

    public static void main(String[] args) throws InterruptedException {
        ObservableList<String> observableList = new ObservableList<>();

        observableList.getObservable().subscribe(System.out::println);

        observableList.add("One");
        TimeUnit.SECONDS.sleep(1);
        observableList.add("Two");
        TimeUnit.SECONDS.sleep(1);
        observableList.add("Three");
    }
}
