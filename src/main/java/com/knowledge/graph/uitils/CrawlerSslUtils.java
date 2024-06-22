package com.knowledge.graph.uitils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 忽略ssh
 * https://blog.csdn.net/u010515202/article/details/115697868
 *
 * @author JUE
 */
public class CrawlerSslUtils {

    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     */
    public static void ignoreSsl() {
        trustAllHttpsCertificates();
    }

    private static void trustAllHttpsCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[1];
            TrustManager tm = new MyTrustManager();
            trustAllCerts[0] = tm;
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((urlHostName, session) -> {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyTrustManager implements TrustManager, X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
