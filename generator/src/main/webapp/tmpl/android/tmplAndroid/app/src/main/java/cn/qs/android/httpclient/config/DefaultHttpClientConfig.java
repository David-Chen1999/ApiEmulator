package cn.qs.android.httpclient.config;

import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/31.
 */
public class DefaultHttpClientConfig implements HttpClientConfig {

    private boolean debugMode = false;
    private boolean runOnUiThread = true;
    private int connectTimeOutSeconds = 6;
    private int readTimeOutSeconds = 6;
    private int writeTimeOutSeconds = 6;

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean runOnUiThread() {
        return runOnUiThread;
    }

    public int getConnectTimeOutSeconds() {
        return connectTimeOutSeconds;
    }

    public int getReadTimeOutSeconds() {
        return readTimeOutSeconds;
    }

    public int getWriteTimeOutSeconds() {
        return writeTimeOutSeconds;
    }

    public void configGsonWithBuilder(GsonBuilder builder) {
        builder.setDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public void configSupportMediaTypes(Map<String, String> map) {
        map.put("*", "application/octet-stream");
        map.put("png", "image/png");
        map.put("jpg", "image/jpeg");
        map.put("jpeg", "image/jpeg");
        map.put("wav", "audio/wav");
        map.put("mp3", "audio/mp3");
        map.put("mp4", "video/mpeg4");
        map.put("txt", "text/plain");
        map.put("xls", "application/x-xls");
        map.put("xml", "text/xml");
        map.put("apk", "application/vnd.android.package-archive");
        map.put("doc", "application/msword");
        map.put("pdf", "application/pdf");
        map.put("html", "text/html");
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory() {
        return null;
    }

    @Override
    public X509TrustManager getX509TrustManager() {
        return null;
    }

    @Override
    public boolean hostnameVerifier(String hostname) {
        return true;
    }


    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setConnectTimeOutSeconds(int connectTimeOutSeconds) {
        this.connectTimeOutSeconds = connectTimeOutSeconds;
    }

    public void setReadTimeOutSeconds(int readTimeOutSeconds) {
        this.readTimeOutSeconds = readTimeOutSeconds;
    }

    public void setWriteTimeOutSeconds(int writeTimeOutSeconds) {
        this.writeTimeOutSeconds = writeTimeOutSeconds;
    }

}
