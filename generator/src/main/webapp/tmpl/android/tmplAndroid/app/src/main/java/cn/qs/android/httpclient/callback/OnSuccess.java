package cn.qs.android.httpclient.callback;

import okhttp3.Headers;

/**
 * Created by 15735 on 2017/1/3.
 */

public interface OnSuccess<T> {

    void onSuccess(int status, Headers headers, T body);

}
