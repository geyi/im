package com.airing.im.utils.http;

import com.airing.im.constant.Common;
import com.airing.im.enums.ResponseState;
import com.airing.im.exceptions.RestException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);
    private static final HttpClient HTTP_CLIENT = new HttpClient();

    public static String get(String url, ResponseState exception) {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpRequest = new HttpGet(url);
            CloseableHttpClient client = HTTP_CLIENT.getHttpClient(10000, 10000);
            response = client.execute(httpRequest);
            int stateCode = response.getStatusLine().getStatusCode();
            byte[] resultByte = EntityUtils.toByteArray(response.getEntity());
            String resultStr = new String(resultByte, Common.DEFAULT_CHARSET);
            if (stateCode == 200) {
                log.info("url:{}, response:{}", url, resultStr);
            } else {
                log.error("请求失败，url：{}，状态码：{}，错误信息：{}", url, stateCode, resultStr);
            }
            return resultStr;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestException(exception);
        } finally {
            if (response != null) {
                try {
                    response.getEntity().getContent().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static String post(String url, String jsonParams, ResponseState exception) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpRequest = new HttpPost(url);
            CloseableHttpClient client = HTTP_CLIENT.getHttpClient(10000, 10000);
            // 设置请求头参数
            httpRequest.addHeader("Content-type", "application/json; charset=utf-8");
            httpRequest.setHeader("Accept", "application/json");
            if (jsonParams != null) {
                httpRequest.setEntity(new StringEntity(jsonParams, Charset.forName(Common.DEFAULT_CHARSET)));
            }
            response = client.execute(httpRequest);
            int stateCode = response.getStatusLine().getStatusCode();
            byte[] resultByte = EntityUtils.toByteArray(response.getEntity());
            String resultStr = new String(resultByte, Common.DEFAULT_CHARSET);
            if (stateCode == 200) {
                log.info("url:{}, response:{}", url, resultStr);
            } else {
                log.error("请求失败，url：{}，状态码：{}，错误信息：{}", url, stateCode, resultStr);
            }
            return resultStr;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestException(exception);
        } finally {
            if (response != null) {
                try {
                    response.getEntity().getContent().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static String post(String url, Map<String, String> headerParams, String jsonParams,
                              ResponseState exception) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpRequest = new HttpPost(url);
            CloseableHttpClient client = HTTP_CLIENT.getHttpClient(10000, 10000);
            // 设置请求头参数
            httpRequest.addHeader("Content-Type", "application/json; charset=utf-8");
            httpRequest.setHeader("Accept", "application/json");
            if (headerParams != null && headerParams.size() > 0) {
                for (Map.Entry<String, String> key : headerParams.entrySet()) {
                    String headerKey = key.getKey();
                    httpRequest.setHeader(headerKey, headerParams.get(headerKey));
                }
            }
            if (jsonParams != null) {
                httpRequest.setEntity(new StringEntity(jsonParams, Charset.forName(Common.DEFAULT_CHARSET)));
            }
            response = client.execute(httpRequest);
            int stateCode = response.getStatusLine().getStatusCode();
            byte[] resultByte = EntityUtils.toByteArray(response.getEntity());
            String resultStr = new String(resultByte, Common.DEFAULT_CHARSET);
            if (stateCode == 200) {
                log.info("url:{}, response:{}", url, resultStr);
            } else {
                log.error("请求失败，url：{}，状态码：{}，错误信息：{}", url, stateCode, resultStr);
            }
            return resultStr;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestException(exception);
        } finally {
            if (response != null) {
                try {
                    response.getEntity().getContent().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static String postForm(String url, Map<String, String> headerParams, Map<String, String> params,
                                  ResponseState exception) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpRequest = new HttpPost(url);
            CloseableHttpClient client = HTTP_CLIENT.getHttpClient(10000, 10000);
            if (headerParams != null && headerParams.size() > 0) {
                for (Map.Entry<String, String> key : headerParams.entrySet()) {
                    String headerKey = key.getKey();
                    httpRequest.setHeader(headerKey, headerParams.get(headerKey));
                }
            }
            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            Set<Map.Entry<String, String>> paramsSet = params.entrySet();
            for (Map.Entry<String, String> param : paramsSet) {
                pairList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            httpRequest.setEntity(new UrlEncodedFormEntity(pairList, Common.DEFAULT_CHARSET));
            response = client.execute(httpRequest);
            int stateCode = response.getStatusLine().getStatusCode();
            byte[] resultByte = EntityUtils.toByteArray(response.getEntity());
            String resultStr = new String(resultByte, Common.DEFAULT_CHARSET);
            if (stateCode == 200) {
                log.info("url:{}, response:{}", url, resultStr);
            } else {
                log.error("请求失败，url：{}，状态码：{}，错误信息：{}", url, stateCode, resultStr);
            }
            return resultStr;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestException(exception);
        } finally {
            if (response != null) {
                try {
                    response.getEntity().getContent().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
