package cn.qs.android.httpclient;

import android.util.Log;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2015/10/12
 * 描    述：日志的工具类
 * 修订历史：
 * ================================================
 */
public class HttpLogger {

    private boolean enable = false;

    private String tag = "HttpClient";

    public HttpLogger() {
    }

    public HttpLogger(String tag) {
        this.tag = tag;
    }

    public HttpLogger(boolean isLogEnable, String tag) {
        this.enable = isLogEnable;
        this.tag = tag;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void v(String msg) {
        v(tag, msg);
    }

    public void v(String tag, String msg) {
        if (enable) Log.v(tag, msg);
    }

    public void d(String msg) {
        d(tag, msg);
    }

    public void d(String tag, String msg) {
        if (enable) Log.d(tag, msg);
    }

    public void i(String msg) {
        i(tag, msg);
    }

    public void i(String tag, String msg) {
        if (enable) Log.i(tag, msg);
    }

    public void w(String msg) {
        w(tag, msg);
    }

    public void w(String tag, String msg) {
        if (enable) Log.w(tag, msg);
    }

    public void e(String msg) {
        e(tag, msg);
    }

    public void e(String tag, String msg) {
        if (enable) Log.e(tag, msg);
    }

    public void e(String msg, Throwable tr) {
        if (enable) Log.e(tag, msg, tr);
    }

    public void e(Throwable t) {
        if (enable) t.printStackTrace();
    }

}