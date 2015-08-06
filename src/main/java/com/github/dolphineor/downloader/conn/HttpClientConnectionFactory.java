package com.github.dolphineor.downloader.conn;

import org.apache.http.HttpRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import static com.github.dolphineor.core.ElasticCrawler.CONFIG;

/**
 * {@code HttpClientConnectionFactory} maintains a pool of
 * {@link CloseableHttpClient}s and is able to service connection requests
 * from multiple execution threads. Connections are pooled on a per route
 * basis. A request for a route which already the manager has persistent
 * connections for available in the pool will be services by leasing
 * a connection from the pool rather than creating a brand new connection.
 *
 * @author dolphineor
 */
public class HttpClientConnectionFactory {

    private final PoolingHttpClientConnectionManager connectionManager;


    private HttpClientConnectionFactory() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(CONFIG.getInt("elasticCrawler.http.poolSize"));
        connectionManager.setDefaultMaxPerRoute(CONFIG.getInt("elasticCrawler.http.maxPerRoute"));
    }

    public static HttpClientConnectionFactory createPool() {
        return new HttpClientConnectionFactory();
    }

    public CloseableHttpClient createHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
        if (CONFIG.getIsNull("elasticCrawler.http.userAgent")) {
            httpClientBuilder.setUserAgent("");
        } else {
            httpClientBuilder.setUserAgent(CONFIG.getString("elasticCrawler.http.userAgent"));
        }

        if (CONFIG.getBoolean("elasticCrawler.http.gzip")) {
            httpClientBuilder.addInterceptorFirst((HttpRequest request, HttpContext context) -> {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }

            });
        }

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(CONFIG.getBoolean("elasticCrawler.http.socketKeepAlive"))
                .setTcpNoDelay(CONFIG.getBoolean("elasticCrawler.http.tcpNoDelay")).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(CONFIG.getInt("elasticCrawler.http.retryTimes"), true));
        return httpClientBuilder.build();
    }

}
