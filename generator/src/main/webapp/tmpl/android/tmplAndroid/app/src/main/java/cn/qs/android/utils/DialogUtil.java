package cn.qs.android.utils;

/**
 * Created by Bochi on 2017/1/17.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class DialogUtil {

    // 定义一个显示消息的对话框

    /**
     * @param ctx           上下文
     * @param msg           message显示内容
     * @param sureListener  确认点击事件
     * @param falseListener 取消点击事件
     */
    public static void showDialog(final Context ctx
            , String msg, OnClickListener sureListener, OnClickListener falseListener) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setMessage(msg).setCancelable(false);
        if (sureListener != null)
            builder.setPositiveButton("确定", sureListener);
        if (falseListener != null)
            builder.setNegativeButton("取消", falseListener);
        builder.create().show();
    }

    // 定义一个显示指定组件的对话框
    public static void showDialog(Context ctx, View view) {
        new AlertDialog.Builder(ctx)
                .setView(view).setCancelable(false)
                .setPositiveButton("确定", null)
                .create()
                .show();
    }
}
