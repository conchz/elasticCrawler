package org.ec.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.ec.downloader.HttpClientDownloader;
import org.ec.extractor.HtmlExtractor;
import org.ec.scheduler.Task;
import org.ec.scheduler.TaskMaster;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
        TaskMaster taskMaster = TaskMaster.build();
        taskMaster.setWorkerThread(2).schedule(tasks);
        taskMaster.start();
        taskMaster.shutdown();

    }
}
