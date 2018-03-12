package cn.qs.android.httpclient.exception;

/**
 * Created by 15735 on 2016/8/31.
 */
public class HttpClientSSLConfigException extends HttpClientException {

    public HttpClientSSLConfigException(String detailMessage) {
        super(detailMessage);
    }

    public HttpClientSSLConfigException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
