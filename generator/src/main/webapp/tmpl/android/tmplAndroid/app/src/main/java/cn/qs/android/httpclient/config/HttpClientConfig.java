package cn.qs.android.httpclient.config;

import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/31.
 */
public interface HttpClientConfig {

    public boolean isDebugMode();

    public boolean runOnUiThread();

    public int getConnectTimeOutSeconds();

    public int getReadTimeOutSeconds();

    public int getWriteTimeOutSeconds();

    /**
     * 配置解析返回数据的Gson
     * */
    public void configGsonWithBuilder(GsonBuilder builder);

    /***
     * HTTP Content-type 对照表
     * 参考：http://tool.oschina.net/commons
     * */
    public void configSupportMediaTypes(Map<String, String> map);

    public SSLSocketFactory getSSLSocketFactory();

    public X509TrustManager getX509TrustManager();

    public boolean hostnameVerifier(String hostname);

}
