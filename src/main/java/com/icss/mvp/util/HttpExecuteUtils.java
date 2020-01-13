package com.icss.mvp.util;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.entity.response.ExecuteResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ray on 2018/4/20.
 */
@Component
public class HttpExecuteUtils {

    private static Logger logger = Logger.getLogger(HttpExecuteUtils.class);

    /**
     * @param url
     * @param params
     * @return
     */
    public static String httpGet(String url, Map<String, Object> params) {
        return executeHttpGet(url, params, null).getHttpEntity();
    }

    /**
     * <pre>
     *     args proxy代理配置，可选，仅支持以下两种格式，4或6个参数
     *     args[0] proxy_host
     *     args[1] proxy_port
     *     args[2] username
     *     args[3] password
     *     args[4] workstation 可选
     *     args[5] domain 可选
     *     传入 workstation domain 时，进行 NTLM认证，创建 NTCredentials，
     *     未传入，则进行 Basic认证，创建 UsernamePasswordCredentials
     * </pre>
     *
     * @param url
     * @param params
     * @param cookie
     * @param args proxy_host, proxy_port, username, password, [workstation , domain]
     * @return
     */
    public static String httpGet(String url, Map<String, Object> params, Map<String, String> cookie, String... args) {
        return executeHttpGet(url, params, cookie, args).getHttpEntity();
    }

    /**
     * <pre>
     *     args proxy代理配置，可选，仅支持以下两种格式，4或6个参数
     *     args[0] proxy_host
     *     args[1] proxy_port
     *     args[2] username
     *     args[3] password
     *     args[4] workstation 可选
     *     args[5] domain 可选
     *     传入 workstation domain 时，进行 NTLM认证，创建 NTCredentials，
     *     未传入，则进行 Basic认证，创建 UsernamePasswordCredentials
     * </pre>
     *
     * @param url
     * @param params
     * @param cookie
     * @param args proxy_host, proxy_port, username, password, [workstation , domain]
     * @return
     */
    public static ExecuteResponse executeHttpGet(String url, Map<String, Object> params, Map<String, String> cookie,
                                                 String... args) {
        HttpGet httpGet = new HttpGet(url + "?" + generateQueryString(params));
        if (cookie != null && !cookie.isEmpty()) {
            httpGet.addHeader(new BasicHeader("Cookie", generateHeaderCookie(cookie)));
        }

        HttpHost proxy = args.length > 1 ? new HttpHost(args[0], MathUtils.parseIntSmooth(args[1])) : null;
        RequestConfig config = buildConfig(proxy, 1000 * 60 * 60, 1000 * 60 * 60);
        CredentialsProvider provider = args.length > 0 ? buildProvider(args) : null;

        return executeHttpRequest(httpGet, config, provider);
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public static String httpPost(String url, Map<String, Object> params) {
        return executeHttpPost(url, params, null).getHttpEntity();
    }

    /**
     * <pre>
     *     args proxy代理配置，可选，仅支持以下两种格式，4或6个参数
     *     args[0] proxy_host
     *     args[1] proxy_port
     *     args[2] username
     *     args[3] password
     *     args[4] workstation 可选
     *     args[5] domain 可选
     *     传入 workstation domain 时，进行 NTLM认证，创建 NTCredentials，
     *     未传入，则进行 Basic认证，创建 UsernamePasswordCredentials
     * </pre>
     *
     * @param url
     * @param params
     * @param cookie
     * @param args proxy_host, proxy_port, username, password, [workstation , domain]
     * @return
     */
    public static String httpPost(String url, Map<String, Object> params, Map<String, String> cookie, String... args) {
        return executeHttpPost(url, params, cookie, args).getHttpEntity();
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public static ExecuteResponse executeHttpPost(String url, Map<String, Object> params) {
        return executeHttpPost(url, params, null);
    }

    /**
     * <pre>
     *     args proxy代理配置，可选，仅支持以下两种格式，4或6个参数
     *     args[0] proxy_host
     *     args[1] proxy_port
     *     args[2] username
     *     args[3] password
     *     args[4] workstation 可选
     *     args[5] domain 可选
     *     传入 workstation domain 时，进行 NTLM认证，创建 NTCredentials，
     *     未传入，则进行 Basic认证，创建 UsernamePasswordCredentials
     * </pre>
     *
     * @param url
     * @param params
     * @param cookie
     * @param args proxy_host, proxy_port, username, password, [workstation , domain]
     * @return
     */
    public static ExecuteResponse executeHttpPost(String url, Map<String, Object> params, Map<String, String> cookie,
                                                  String... args) {
        HttpPost httpPost = new HttpPost(url);

        setJsonEntity(params, httpPost);

        if (cookie != null && !cookie.isEmpty()) {
            httpPost.addHeader(new BasicHeader("Cookie", generateHeaderCookie(cookie)));
        }

        HttpHost proxy = args.length > 1 ? new HttpHost(args[0], MathUtils.parseIntSmooth(args[1])) : null;
        RequestConfig config = buildConfig(proxy, 1000 * 60 * 60, 1000 * 60 * 60);
        httpPost.setConfig(config);

        CredentialsProvider provider = args.length > 0 ? buildProvider(args) : null;

        return executeHttpRequest(httpPost, config, provider);
    }

    /**
     * <pre>
     *     args proxy代理配置，可选，仅支持以下两种格式，4或6个参数
     *     args[0] proxy_host
     *     args[1] proxy_port
     *     args[2] username
     *     args[3] password
     *     args[4] workstation 可选
     *     args[5] domain 可选
     *     传入 workstation domain 时，进行 NTLM认证，创建 NTCredentials，
     *     未传入，则进行 Basic认证，创建 UsernamePasswordCredentials
     * </pre>
     *
     * @param url
     * @param params
     * @param cookie
     * @param args proxy_host, proxy_port, username, password, [workstation , domain]
     * @return
     */
    public static ExecuteResponse encodedHttpPost(String url, Map<String, Object> params, Map<String, String> cookie,
                                                  String... args) {
        HttpPost httpPost = new HttpPost(url);

        setUrlFormEntity(params, httpPost);

        if (cookie != null && !cookie.isEmpty()) {
            httpPost.addHeader(new BasicHeader("Cookie", generateHeaderCookie(cookie)));
        }

        HttpHost proxy = args.length > 1 ? new HttpHost(args[0], MathUtils.parseIntSmooth(args[1])) : null;
        RequestConfig config = buildConfig(proxy, 1000 * 60 * 60, 1000 * 60 * 60);
        httpPost.setConfig(config);

        CredentialsProvider provider = args.length > 0 ? buildProvider(args) : null;

        return executeHttpRequest(httpPost, config, provider);
    }

    private static String generateQueryString(Map<String, Object> parameters) {
        String urlParameters = "";
        // 拼接参数
        if (parameters != null && !parameters.isEmpty()) {
            List<NameValuePair> list = parameters.entrySet().stream().map(entry -> new BasicNameValuePair(
                                                                                                          entry.getKey(),
                                                                                                          entry.getValue().toString())).collect(Collectors.toList());

            try {
                urlParameters = EntityUtils.toString(new UrlEncodedFormEntity(list, StandardCharsets.UTF_8));
            } catch (IOException e) {
                logger.error("new UrlEncodedFormEntity exception, error: "+e.getMessage());
            }
        }
        return urlParameters;
    }

    private static void setUrlFormEntity(Map<String, Object> parameters, HttpPost httpPost) {
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String entryValue = entry.getValue() != null ? String.valueOf(entry.getValue()) : "";
            pairs.add(new BasicNameValuePair(entry.getKey(), entryValue));
        }

        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
    }

    private static void setJsonEntity(Map<String, Object> parameters, HttpPost httpPost) {
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        // stringEntity.setContentType("text/json");
        // stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        httpPost.setEntity(new StringEntity(JSON.toJSONString(parameters), StandardCharsets.UTF_8));
    }

    private static String generateHeaderCookie(Map<String, String> cookie) {
        StringBuilder headerCookies = new StringBuilder();
        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            headerCookies.append(entry.getKey()).append('=').append(entry.getValue()).append(";");
        }
        return headerCookies.toString();
    }

    /**
     * @param proxy
     * @param timeout socketTimeout connectTimeout connectionRequestTimeout
     * @return
     */
    public static RequestConfig buildConfig(HttpHost proxy, int... timeout) {
        /**
         * 从连接池中获取连接的超时时间
         *
         * <pre>
         *     假设：连接池中已经使用的连接数等于setMaxTotal，新来的线程在等待1*1000后超时，
         *     错误内容：org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
         * </pre>
         */
        int connectionRequestTimeout = (1000);

        /**
         * 通过网络与服务器建立连接的超时时间
         *
         * <pre>
         *     Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间。
         *     假设：访问一个IP，192.168.10.100，这个IP不存在或者响应太慢，
         *     那么将会返回 java.net.SocketTimeoutException: connect timed out
         * </pre>
         */
        int connectTimeout = (5 * 1000);

        /**
         * 指的是连接上一个url，获取response的返回等待时间
         *
         * <pre>
         *     假设：url程序中存在阻塞、或者response 返回的文件内容太大，在指定的时间内没有读完，
         *     则出现java.net.SocketTimeoutException: Read timed out
         * </pre>
         */
        int socketTimeout = (60 * 1000);

        if (timeout.length == 3 || timeout.length == 2) {
            socketTimeout = timeout[0];
            connectTimeout = timeout[1];
            connectionRequestTimeout = timeout.length == 3 ? timeout[2] : connectionRequestTimeout;
        }

        // 设置代理IP、端口、协议（请分别替换）
        // HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");

        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        if (proxy != null) {
            // 把代理设置到请求配置
            config = RequestConfig.copy(config).setProxy(proxy).build();
        }

        return config;
    }

    /**
     * <pre>
     *     args proxy代理配置，仅支持以下两种格式，4或6个参数，参数顺序不可调换
     *     args[0] proxy_host
     *     args[1] proxy_port
     *     args[2] username
     *     args[3] password
     *     args[4] workstation 可选
     *     args[5] domain 可选
     *     传入 workstation domain 时，进行 NTLM认证，创建 NTCredentials，
     *     未传入，则进行 Basic认证，创建 UsernamePasswordCredentials
     * </pre>
     *
     * @param args userName password workstation|host domain|port
     * @return
     */
    private static CredentialsProvider buildProvider(String... args) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        if (args.length < 4) {
            return provider;
        }

        int port = MathUtils.parseIntSmooth(args[1]);
        AuthScope scope = new AuthScope(args[0], port > 0 ? port : AuthScope.ANY_PORT);

        if (args.length > 4) {
            NTCredentials credentials = new NTCredentials(args[2], args[3], args[4], args[5]);
            // scope = AuthScope.ANY;
            provider.setCredentials(scope, credentials);
        } else {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(args[2], args[3]);
            provider.setCredentials(scope, credentials);
        }
        return provider;
    }

    private static ExecuteResponse executeHttpRequest(HttpRequestBase request, RequestConfig config,
                                                      CredentialsProvider provider) {
        ExecuteResponse result = new ExecuteResponse();

        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = buildClient(cookieStore, config, provider);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);

            result = getExecuteResponse(response, request);

            List<Cookie> cookies = cookieStore.getCookies();
            result.setCookieStore(cookies.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue,
                                                                            (o1, o2) -> o1 + o2)));
        } catch (Exception e) {
            logger.error("cookieStore.getCookies exception, error: "+e.getMessage());
            if (response == null) {
                result.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("response.close exception, error: "+e.getMessage());
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("client.close exception, error: "+e.getMessage());
                }
            }
        }

        return result;
    }

    public static CloseableHttpClient buildClient(CookieStore cookie, RequestConfig config, CredentialsProvider provider) {
        if (provider == null) {
            return buildClient(cookie, config);
        }

        CookieStore cookieStore = cookie != null ? cookie : new BasicCookieStore();
        return HttpClients.custom().setDefaultCookieStore(cookieStore).setDefaultCredentialsProvider(provider).setDefaultRequestConfig(config).build();
    }

    public static CloseableHttpClient buildClient(CookieStore cookie, RequestConfig config) {
        CookieStore cookieStore = cookie != null ? cookie : new BasicCookieStore();
        return HttpClients.custom().setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build();
    }

    private static ExecuteResponse getExecuteResponse(CloseableHttpResponse response, HttpRequestBase httpRequest) {
        ExecuteResponse result = new ExecuteResponse();

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            result.setStatusCode(statusCode);

            if (statusCode != HttpStatus.SC_OK) {
                httpRequest.abort();
                logger.error("HttpClient execute error, status code: " + statusCode);
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String entityString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//                    logger.info("HttpClient execute response: " + entityString);
                    result.setHttpEntity(entityString);
                }
            }
        } catch (IOException e) {
            logger.error("response.getEntity exception, error: "+e.getMessage());
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                logger.error("EntityUtils.consume exception, error: "+e.getMessage());
            }
        }

        return result;
    }

    public static <T> T parseResponse(String responseEntity, Class<T> clazz, T defaultValue) {
        T result;

        try {
            result = JSON.parseObject(responseEntity, clazz);
        } catch (Exception e) {
            result = null;
        }

        return result == null ? defaultValue : result;
    }
}
