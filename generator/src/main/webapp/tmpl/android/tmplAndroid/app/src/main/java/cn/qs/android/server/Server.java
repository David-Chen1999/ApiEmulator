package cn.qs.android.server;


import cn.qs.android.httpclient.HttpClient;
import cn.qs.android.httpclient.config.DefaultHttpClientConfig;

/**
 * Created by 15735 on 2017/1/4.
 */

public class Server {
    @SuppressWarnings("unused")
    private static final String TAG = Server.class.getSimpleName();

    static {
        HttpClient.config(new DefaultHttpClientConfig() {
            @Override
            public boolean isDebugMode() {
                return true;
            }

            @Override
            public int getConnectTimeOutSeconds() {
                return 10;
            }

            @Override
            public int getReadTimeOutSeconds() {
                return 15;
            }

            @Override
            public int getWriteTimeOutSeconds() {
                return 30;
            }

            @Override
            public boolean runOnUiThread() {
                return true;
            }
        });
    }

}
