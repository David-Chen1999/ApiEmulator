package cn.qs.android.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 更新管理器
 * <p>
 */
@SuppressWarnings("unused")
public class UpdateAppUtils {

    @SuppressWarnings("unused")
    private static final String TAG = UpdateAppUtils.class.getSimpleName();

    // 储存下载的Id
    public static final String DOWNLOAD_APK_ID_PREFS = "cn.qs.dataacquisition.download_apk_id_prefs";

    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context    上下文
     * @param updateInfo 更新信息
     * @param infoName   通知名称
     * @param storeApk   存储的Apk
     */
    @SuppressWarnings("unused")
    public static void downloadApk(
            Context context, LatestAppInfo updateInfo,
            String infoName, String storeApk
    ) {
        if (!isDownloadManagerAvailable()) {
            return;
        }

        String description = updateInfo.description;
        String appUrl = updateInfo.appURL;

        if (appUrl == null || appUrl.isEmpty()) {
            Log.e(TAG, "请填写\"App下载地址\"");
            return;
        }

        appUrl = appUrl.trim(); // 去掉首尾空格

        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加Http信息
        }

        Log.e(TAG, "appUrl: " + appUrl);

        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        request.setTitle(infoName);
        request.setDescription(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);

        Context appContext = context.getApplicationContext();
        DownloadManager manager = (DownloadManager)
                appContext.getSystemService(Context.DOWNLOAD_SERVICE);

        // 存储下载Key
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        sp.edit().putLong(DOWNLOAD_APK_ID_PREFS, manager.enqueue(request)).apply();
    }

    // 最小版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }


    /**
     * 安装Apk
     *
     * @param context
     * @param downloadApkId the id of the downloaded file.
     */
    public static void installApk(Context context, long downloadApkId) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long id = sp.getLong(DOWNLOAD_APK_ID_PREFS, -1L);

        if (downloadApkId == id) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Log.e(TAG, "下载失败");
            }
        }
    }
}
