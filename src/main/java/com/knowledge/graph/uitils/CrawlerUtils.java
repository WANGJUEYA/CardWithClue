package com.knowledge.graph.uitils;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class CrawlerUtils {

    private CrawlerUtils() {
    }

    public static Document getHtml(String url, String headerKey, String headerValue) {
        try {
            return Jsoup.connect(url).header(headerKey, headerValue).execute().parse();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getJsonStr(String url, String requestMethod, String headerKey, String headerValue) {
        try {
            CrawlerSslUtils.ignoreSsl();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            // 设置请求方法
            connection.setRequestMethod(StringUtils.defaultIfBlank(requestMethod, "GET"));
            /// 请求头
            connection.setRequestProperty(headerKey, headerValue);
            // 发送请求
            connection.connect();
            // 检查响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String jsonStr = CharStreams.fromStream(connection.getInputStream()).toString();
                log.debug("{} >>> \n{}", url, jsonStr);
                return jsonStr;
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
