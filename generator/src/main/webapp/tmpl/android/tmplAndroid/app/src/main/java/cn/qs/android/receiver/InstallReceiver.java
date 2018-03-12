package cn.qs.android.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.qs.android.utils.UpdateAppUtils;

/**
 * 安装下载接收器
 */
public class InstallReceiver extends BroadcastReceiver {

    private static final String TAG =
              InstallReceiver.class.getSimpleName();

    // 安装下载接收器
    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            UpdateAppUtils.installApk(context, downloadApkId);
        }
    }

}
