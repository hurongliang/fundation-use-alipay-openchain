package com.hurl.fundationusealipayopenchain.config;

import com.antfinancial.mychain.baas.tool.restclient.RestClient;
import com.antfinancial.mychain.baas.tool.restclient.RestClientProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//@Configuration
public class RestClientConfig {

//    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().setReadTimeout(Duration.ofSeconds(5)).additionalMessageConverters(new CustomizeHttpMessageConverter()).build();
    }

//    @Bean
    public RestClientProperties restClientProperties() {
        RestClientProperties properties = new RestClientProperties();
        properties.setBizid("a00e36c5");
        properties.setCipherSuit("ec");
        properties.setRestUrl("https://rest.baas.alipay.com");
        properties.setAccessId("b2Ui1PY8SKHQQTGL");
        properties.setAccessSecret("access.key");
        properties.setDefaultAccount("guangda02");
        properties.setDefaultAccountKey("user.key");
        properties.setDefaultAccountPwd("Gd@200713");
        properties.setReadFileFromExt(false);
        return properties;
    }

//    @Bean
    public RestClient restClient(@Autowired RestClientProperties restClientProperties, @Autowired RestTemplate restTemplate) throws IOException {
        int connectionRequestTimeout = restClientProperties.getConnectionRequestTimeout() != 0 ? restClientProperties.getConnectionRequestTimeout() : 3000;
        int connectTimeout = restClientProperties.getConnectTimeout() != 0 ? restClientProperties.getConnectTimeout() : 3000;
        int socketTimeout = restClientProperties.getSocketTimeout() != 0 ? restClientProperties.getSocketTimeout() : 2000;
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        RegistryBuilder rb = RegistryBuilder.create();
        Registry<ConnectionSocketFactory> registry = rb.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
        int maxTotal = restClientProperties.getMaxTotal() != 0 ? restClientProperties.getMaxTotal() : 200;
        int maxPerRoute = restClientProperties.getDefaultMaxPerRoute() != 0 ? restClientProperties.getDefaultMaxPerRoute() : 200;
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
        return new RestClient(restTemplate, restClientProperties);
    }

    public class CustomizeHttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public CustomizeHttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.TEXT_XML);
            mediaTypes.add(MediaType.TEXT_PLAIN);
            mediaTypes.add(MediaType.TEXT_HTML);
            mediaTypes.add(MediaType.APPLICATION_JSON);
            this.setSupportedMediaTypes(mediaTypes);
        }
    }
}
