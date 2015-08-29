package org.ec.downloader;

import org.ec.downloader.conn.HttpClientConnectionFactory;
import org.ec.scheduler.Task;
import org.ec.util.Logs;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * {@code HttpClientDownloader} is a implementation of {@link org.apache.http.client.HttpClient}
 *
 * @author dolphineor
 */
public class HttpClientDownloader extends Logs implements Downloader {

    private static HttpClientConnectionFactory httpClientPool = HttpClientConnectionFactory.createPool();

    private static Downloader downloader;


    private HttpClientDownloader() {
    }


    @Override
    public String download(Task task) throws IOException {
        CloseableHttpClient httpClient = httpClientPool.createHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        logger.debug("Downloading url: " + task.getUrl());
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(task.getUrl()), localContext)) {
            return EntityUtils.toString(response.getEntity(), Charset.forName(task.getCharset()));
        }
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
