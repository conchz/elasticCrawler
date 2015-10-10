package org.spider;

import org.spider.core.ElasticCrawler;
import org.spider.downloader.HttpClientDownloader;
import org.spider.extractor.HtmlExtractor;
import org.spider.scheduler.Task;

import java.util.ArrayList;
import java.util.List;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by dolphineor on 2015-10-10.
 *
 * @author dolphineor
 */
public class ElasticCrawlerTest {

    public static void main(String[] args) throws Exception {
        String scrapeUrl = "http://search.jd.com/Search?keyword=%s&enc=utf-8";

        String[] arr = {"冬装", "毛衣", "羽绒服", "书包", "手套", "夹克", "卫衣", "暖宝宝", "围巾"};
        List<Task> tasks = new ArrayList<>();

        for (String k : arr) {
            Task task = new Task();
            task.setCharset("GBK");
            k = encode(k, UTF_8.name());
            task.setUrl(String.format(scrapeUrl, k));
            task.setDownloader(HttpClientDownloader.create());
            task.setExtractor(HtmlExtractor.create());
            tasks.add(task);
        }


        // start a elasticCrawler
        ElasticCrawler crawler = ElasticCrawler.create().addTask(tasks);
        crawler.runAsync();
    }
}
