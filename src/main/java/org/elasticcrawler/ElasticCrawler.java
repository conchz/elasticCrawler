package org.elasticcrawler;

import org.elasticcrawler.core.Task;
import org.elasticcrawler.core.TaskMaster;
import org.elasticcrawler.downloader.KissyDownloader;
import org.elasticcrawler.extractor.HtmlExtractor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dolphineor on 2015-1-18.
 */
public class ElasticCrawler {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String[] arr = {"冬装", "毛衣", "羽绒服", "书包", "手套", "夹克", "卫衣", "暖宝宝", "围巾"};
        List<Task> tasks = new ArrayList<>();
        for (String k : arr) {
            Task task = new Task();
            task.setCharset("UTF-8");
            k = java.net.URLEncoder.encode(k, "UTF-8");
//            task.setUrl("http://s.taobao.com/search?q=" + k);
            task.setUrl("http://search.jd.com/Search?keyword=" + k + "&enc=utf-8");
            task.setDownloader(KissyDownloader.create());
            task.setExtractor(HtmlExtractor.create());
            tasks.add(task);
        }


        // start a elasticCrawler
        TaskMaster taskMaster = TaskMaster.build();
        taskMaster.setWorkerThread(4).schedule(tasks);
        taskMaster.start();

    }
}
