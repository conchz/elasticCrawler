package org.elasticcrawler;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.elasticcrawler.core.Site;
import org.elasticcrawler.downloader.HttpClientDownloader;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by baizz on 2015-1-17.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Site site = new Site();
        site.setDomain("www2.baidu.com");
        site.setCharset("UTF-8");
        site.setRetryTimes(3);

        CloseableHttpClient httpClient = new HttpClientDownloader().getHttpClient(site);
        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet("http://www2.baidu.com"), localContext)) {
            System.out.println(EntityUtils.toString(response.getEntity(), Charset.forName(site.getCharset())));
        }
    }
}
