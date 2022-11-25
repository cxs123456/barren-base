package org.barren.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author cxs
 **/
@Configuration
public class RestTemplateConfig {


    /**
     * RestTemplate 配置 okHttp
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory ClientHttpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(okHttpClient());
    }


    /**
     * OkHttpClient 配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public okhttp3.OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 配置代理
        // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(properties.getProxyHost(), properties.getProxyPort()));
        // builder.proxy(proxy);

        builder // 设置连接超时
                .connectTimeout(20, TimeUnit.SECONDS)
                // 设置读超时
                .readTimeout(20, TimeUnit.SECONDS)
                // 设置写超时
                .writeTimeout(20, TimeUnit.SECONDS)
                // 是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(20, 5L, TimeUnit.MINUTES));
        return builder.build();

    }
}
