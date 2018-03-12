package cn.qs.android.server.callback;

/**
 * Created by pangpingfei on 2017/1/10.
 */

public class ErrorResponse {
    public int status;
    public int code;
    public String msg;

    public ErrorResponse(){

    }

    public ErrorResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
