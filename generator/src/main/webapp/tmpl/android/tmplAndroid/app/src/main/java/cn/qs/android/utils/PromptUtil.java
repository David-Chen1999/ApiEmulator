package cn.qs.android.utils;

//import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;


/**
 * Created by pangpingfei on 2017/1/9.
 */

public class PromptUtil {

    public static void showToast(Context context, String msg) {
//        Activity activity = null;
//        if (context instanceof Activity) {
//            activity = (Activity) context;
//        }
//        activity.runOnUiThread(() -> {
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
//        });
    }


    // 单个按钮对话框

    public static void showAlert(Context context, String message) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton("确定", null).setCancelable(false).create().show();
    }

    public static void showAlert(Context context, String message, String buttonTitle) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(buttonTitle, null).setCancelable(false).create().show();
    }

    public static void showAlert(Context context, String message, OnClickListener listener) {
        if (listener == null) {
            showAlert(context, message);
            return;
        }
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton("确定", listener).setCancelable(false).create().show();
    }

    public static void showAlert(Context context, String message, String buttonTitle, OnClickListener listener) {
        if (listener == null) {
            showAlert(context, message, buttonTitle);
            return;
        }
        if (buttonTitle == null) {
            showAlert(context, message, listener);
            return;
        }
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(buttonTitle, listener).setCancelable(false).create().show();
    }

    public static void showAlert(Context context, String message, String buttonTitle, OnClickListener listener, boolean cancelable) {
        if (listener == null) {
            showAlert(context, message, buttonTitle);
            return;
        }
        if (buttonTitle == null) {
            showAlert(context, message, listener);
            return;
        }
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(buttonTitle, listener).setCancelable(cancelable).create().show();
    }

    // 两个按钮对话框

    public static void showAlert(Context context, String message, OnClickListener sureListener, OnClickListener falseListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message).setCancelable(false);
        if (sureListener != null)
            builder.setPositiveButton("确定", sureListener);
        if (falseListener != null)
            builder.setNegativeButton("取消", falseListener);
        builder.create().show();
    }

}
