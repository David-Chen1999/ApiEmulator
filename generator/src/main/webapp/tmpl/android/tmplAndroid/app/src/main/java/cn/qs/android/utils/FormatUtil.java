package cn.qs.android.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {

    public static String getTimeStringFromTime(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        Date d = new Date(time);
        return sf.format(d);
    }

    // duration 毫秒
    public static String getDurationString(long duration) {
        long d = duration / 1000 / 60; // 分
        long hour = d / 60;
        long minute = d % 60;
        String result = (hour==0 ? "" : (hour + "小时")) +  (minute==0 ? "" : (minute + "分钟"));
        return TextUtils.isEmpty(result) ? "0分钟" : result;
    }

    public static String getDateTimeStringFromTime(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d = new Date(time);
        return sf.format(d);
    }


}
