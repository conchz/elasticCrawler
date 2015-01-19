package org.elasticcrawler.downloader;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.elasticcrawler.core.Task;

import java.io.IOException;

/**
 * Created by dolphineor on 2015-1-17.
 * <p>
 * 适用于下载经javascript渲染后的页面
 */
public class HtmlUnitDownloader implements Downloader {

    private static Downloader downloader = null;


    private HtmlUnitDownloader() {
    }

    @Override
    public String download(Task task) throws IOException {
        // 模拟一个浏览器
        WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);

        // 设置webClient的相关参数
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        // 模拟浏览器打开一个网址
        HtmlPage htmlPage = webClient.getPage(task.getUrl());
        webClient.waitForBackgroundJavaScript(1000);
        return htmlPage.asXml();
    }

    @Override
    public String getName() {
        return HTML_UNIT_DOWNLOADER;
    }

    public static Downloader create() {
        // DCL
        if (downloader == null) {
            synchronized (HtmlUnitDownloader.class) {
                if (downloader == null)
                    downloader = new HtmlUnitDownloader();
            }
        }
        return downloader;
    }
}
