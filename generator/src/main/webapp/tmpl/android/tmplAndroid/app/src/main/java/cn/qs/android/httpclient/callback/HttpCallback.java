package cn.qs.android.httpclient.callback;

import okhttp3.Headers;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/30.
 */
public interface HttpCallback<T, M>  {

    void onSuccess(int status, Headers headers, T body);

    void onFailure(int status, Headers headers, M body);

    void onException(Exception e);

}
