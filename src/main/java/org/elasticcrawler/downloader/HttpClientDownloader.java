package org.elasticcrawler.downloader;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.elasticcrawler.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class HttpClientDownloader implements Downloader {

    private static Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);

    private static Downloader downloader = null;

    private final ConcurrentHashMap<String, CloseableHttpClient> httpClients = new ConcurrentHashMap<>();

    private HttpClientFactory httpClientFactory = new HttpClientFactory().setPoolSize(32);


    public CloseableHttpClient getHttpClient(Task task) {
        String url = task.getUrl();
        return httpClients.computeIfAbsent(url, k -> httpClientFactory.createHttpClient(task));
    }

    @Override
    public String download(Task task) throws IOException {
        CloseableHttpClient httpClient = getHttpClient(task);
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(task.getUrl()), localContext)) {
            return EntityUtils.toString(response.getEntity(), Charset.forName(task.getCharset()));
        }
    }

    @Override
    public String getName() {
        return DEFAULT_DOWNLOADER;
    }

    public static Downloader create() {
        // DCL
        if (downloader == null) {
            synchronized (HttpClientDownloader.class) {
                if (downloader == null)
                    downloader = new HttpClientDownloader();
            }
        }
        return downloader;
    }
}
