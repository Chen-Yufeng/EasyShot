package com.ifchan.easyshot.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class BitmapUtil {
    public static void saveBitmapToFile(Bitmap bitmap, int quality) {
        File file = new File(Environment.getExternalStorageDirectory() + "/EasyShot/" + new Date
                ().getTime() + ".JPEG");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
