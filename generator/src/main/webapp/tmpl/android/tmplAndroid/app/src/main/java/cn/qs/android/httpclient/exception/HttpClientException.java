package cn.qs.android.httpclient.exception;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/30.
 */
public class HttpClientException extends RuntimeException {

    public HttpClientException(String detailMessage) {
        super(detailMessage);
    }

    public HttpClientException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
