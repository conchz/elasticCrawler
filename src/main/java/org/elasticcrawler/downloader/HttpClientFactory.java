package org.elasticcrawler.downloader;

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
import org.elasticcrawler.core.Task;

/**
 * Created by dolphineor on 2015-1-17.
 */
public class HttpClientFactory {

    private PoolingHttpClientConnectionManager connectionManager;


    public HttpClientFactory() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    public HttpClientFactory setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient createHttpClient(Task task) {
        return generateClient(task);
    }

    private CloseableHttpClient generateClient(Task task) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
        if (task.getUserAgent() != null) {
            httpClientBuilder.setUserAgent(task.getUserAgent());
        } else {
            httpClientBuilder.setUserAgent("");
        }
        if (task.isUseGzip()) {
            httpClientBuilder.addInterceptorFirst((HttpRequest request, HttpContext context) -> {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }

            });
        }
        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(task.getRetryTimes(), true));
        return httpClientBuilder.build();
    }

}