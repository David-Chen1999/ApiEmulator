package cn.qs.android.httpclient.callback;

import okhttp3.Headers;

/**
 * Created by 15735 on 2017/1/3.
 */

public interface OnFailure<M> {

    void onFailure(int status, Headers headers, M body);

}
