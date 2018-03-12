package cn.qs.android.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pangpingfei on 2017/2/10.
 */

public class FileUtil {


    public static String getImageDir() {
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory() .getAbsolutePath() + "/DataAcquisition/Images/";
            File saveDir = new File(savePath);
            if (!saveDir.exists()) { saveDir.mkdirs(); }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            return null;
        }

        return savePath;
    }

    public static boolean deleteFile(String filePath) {
        if (filePath == null) { return false; }
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean exists(String filePath) {
        if (filePath == null) { return false; }
        File file = new File(filePath);
        return file.isFile() && file.exists();
    }

    public static boolean saveImage(Bitmap bitmap, String path) {
        File file = new File(path);
        if (file.exists()) { file.delete(); }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

}
