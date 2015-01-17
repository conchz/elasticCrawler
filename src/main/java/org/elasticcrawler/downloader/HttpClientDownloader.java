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
import org.elasticcrawler.core.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class HttpClientDownloader {

    private Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);

    private final Map<String, CloseableHttpClient> httpClients = new ConcurrentHashMap<>();

    private HttpClientFactory httpClientFactory = new HttpClientFactory().setPoolSize(32);

    private final ReentrantLock lock = new ReentrantLock();


    public CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientFactory.createHttpClient(null);
        }

        String domain = site.getDomain();
        CloseableHttpClient httpClient;
        lock.lock();
        try {
            httpClient = httpClients.computeIfAbsent(domain, k -> httpClientFactory.createHttpClient(site));
        } finally {
            lock.unlock();
        }

        return httpClient;
    }

    public String download(Site site) throws IOException {
        CloseableHttpClient httpClient = getHttpClient(site);
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet("http://www2.baidu.com"), localContext)) {
            return EntityUtils.toString(response.getEntity(), Charset.forName(site.getCharset()));
        }
    }
}
