package cn.qs.android.httpclient;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by 周旭（Troy.Zhou） on 2016/8/29.
 */
public class TURL {

    private String urlpath;
    private Type successReturnType; // 为null是表示返回的是字符串
    private Type failReturnType;    // 为null是表示返回的是字符串

    public TURL(String urlpath) {
        this.urlpath = urlpath;
        this.successReturnType = null;
        this.failReturnType = null;
    }

    public TURL(String urlpath, TypeToken successReturnTypeToken) {
        this.urlpath = urlpath;
        if (successReturnTypeToken != null) {
            this.successReturnType = successReturnTypeToken.getType();
        }
    }

    public TURL(String urlpath, TypeToken successReturnTypeToken, TypeToken failReturnTypeToken) {
        this.urlpath = urlpath;
        if (successReturnTypeToken != null) {
            this.successReturnType = successReturnTypeToken.getType();
        }
        if (successReturnTypeToken != null) {
            this.failReturnType = failReturnTypeToken.getType();
        }
    }

    public String getUrlpath() {
        return urlpath;
    }

    public void setUrlpath(String urlpath) {
        this.urlpath = urlpath;
    }

    public Type getSuccessReturnType() {
        return successReturnType;
    }

    public void setSuccessReturnType(Type successReturnType) {
        this.successReturnType = successReturnType;
    }

    public Type getFailReturnType() {
        return failReturnType;
    }

    public void setFailReturnType(Type failReturnType) {
        this.failReturnType = failReturnType;
    }

}
