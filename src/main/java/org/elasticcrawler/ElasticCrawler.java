package org.elasticcrawler;

import com.google.common.collect.Lists;
import org.elasticcrawler.core.Task;
import org.elasticcrawler.core.TaskMaster;
import org.elasticcrawler.downloader.HttpClientDownloader;

import java.util.List;

/**
 * Created by dolphineor on 2015-1-18.
 */
public class ElasticCrawler {

    public static void main(String[] args) {
        Task task = new Task();
        task.setCharset("GBK");
        task.setUrl("http://s.taobao.com/search?q=%B6%AC%D7%B0");
        task.setDownloader(HttpClientDownloader.create());
        List<Task> tasks = Lists.newArrayList(task);
        // start a elasticCrawler
        TaskMaster taskMaster = TaskMaster.build().addTask(tasks).setThread(2);

        taskMaster.run();
    }
}
