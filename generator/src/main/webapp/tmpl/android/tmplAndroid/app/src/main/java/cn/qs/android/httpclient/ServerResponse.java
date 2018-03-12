package cn.qs.android.httpclient;
import java.io.Serializable;

public class ServerResponse implements Serializable{
    public String code;
    public String msg;
    public Boolean success;
}
