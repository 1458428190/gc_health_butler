package com.gdufe.health_butler.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/21 17:21
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final int CONNECTION_TIMEOUT = 2000;

    private static final int SOKCET_TIMEOUT = 2000;

    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;

    private static HttpRequestRetryHandler retryHandler = (exception, executionCount, context) -> false;

    private static SSLContext sslcontext = SSLContexts.createSystemDefault();

    private static Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext))
            .build();

    private static final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

    static {
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(20);
    }

    private static CloseableHttpClient httpClient = HttpClients.custom()
            .setRetryHandler(retryHandler).build();

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String get(String url, Map<String, Object> params, Map<String, Object> headers, HttpHost proxy, String charset) throws HttpException {
        logger.info("do get [url:{}, params:{}, headers:{}, proxy:{}, charset:{}",
                url, JSON.toJSON(params), JSON.toJSON(headers), JSON.toJSON(proxy), charset);
        CloseableHttpResponse response = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(SOKCET_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setProxy(proxy).build();
//            String account = "1", password = "2";
//            UsernamePasswordCredentials upc = new UsernamePasswordCredentials(account, password);
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != params) {
                params.forEach((key, param) -> uriBuilder.addParameter(key, param.toString()));
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            if (null != headers) {
                headers.forEach((key, value) -> httpGet.setHeader(key, value.toString()));
            }
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            return handlerResponse(response, charset);
        } catch (Exception e) {
            logger.error("op_rslt exception:{}", e);
            throw new HttpException(e.getMessage());
        } finally {
            close(response);
        }
    }

    public static String get(String url, Map<String, Object> params, Map<String, Object> headers) throws HttpException {
        return get(url, params, headers, null, DEFAULT_CHARSET);
    }

    public static String get(String url, Map<String, Object> params) throws HttpException {
        return get(url, params, null, null, DEFAULT_CHARSET);
    }

    public static String post(String url, Map<String, Object> params, Map<String, Object> headers, HttpHost proxy, String charset) throws HttpException {
        logger.info("do post [url:{}, params:{}, headers:{}, proxy:{}, charset:{}",
                url, JSON.toJSON(params), JSON.toJSON(headers), JSON.toJSON(proxy), charset);
        CloseableHttpResponse response = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(SOKCET_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setProxy(proxy).build();
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> list = new ArrayList<>();
            if (null != params) {
                params.forEach((key, param) -> list.add(new BasicNameValuePair(key, param.toString())));
            }
            HttpPost httpPost = new HttpPost(uriBuilder.build());
            if (null != headers) {
                headers.forEach((key, value) -> httpPost.setHeader(key, value.toString()));
            }
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            return handlerResponse(response, charset);
        } catch (Exception e) {
            logger.error("op_rslt exception:{}", e);
            throw new HttpException(e.getMessage());
        } finally {
            close(response);
        }
    }

    public static String post(String url, Map<String, Object> params, Map<String, Object> headers) throws HttpException {
        return post(url, params, headers, null, DEFAULT_CHARSET);
    }

    public static String post(String url, Map<String, Object> params) throws HttpException {
        return post(url, params, null, null, DEFAULT_CHARSET);
    }

    public static String request(RequestType type, String url, Map<String, Object> params,
                                 Map<String, Object> headers, HttpHost proxy, String charset) throws HttpException {
        switch (type) {
            case GET:
                return get(url, params, headers, proxy, charset);
            case POST:
                return post(url, params, headers, proxy, charset);
            default:
                throw new HttpException("unsupport method : " + type);
        }
    }

    private static void close(CloseableHttpResponse response) {
        try {
            if (null != response) {
                EntityUtils.consume(response.getEntity());
                response.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String handlerResponse(CloseableHttpResponse response, String charset) throws IOException, ParseException {
        String content;
        int status = response.getStatusLine().getStatusCode();
        if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
            HttpEntity entity = response.getEntity();
            String contentType = null;
            if(null != entity.getContentType()) {
                contentType = entity.getContentType().getValue();
            }
            if(entity.getContentEncoding() != null && "gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())){
                entity = new GzipDecompressingEntity(entity);
            }
            // 根据响应编码设置
            if(null != contentType && contentType.contains("charset")) {
                charset = contentType.split("charset=")[1];
            }
            content = EntityUtils.toString(entity, charset);
            close(response);
            return content;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }

    public enum RequestType {
        GET, POST
    }

    public static void main(String[] args) {
    }
}
