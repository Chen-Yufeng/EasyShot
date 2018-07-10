package com.ifchan.easyshot.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Date;

public enum ScreenshotManager {
    INSTANCE,;
    private static final String SURFACE_CONTROL = "android.view.SurfaceControl";
    private static final String METHOD_NAME = "screenshot";
    private static final Class[] params = new Class[]{
            Integer.TYPE, Integer.TYPE
    };
    private static Class sClass;
    private static Method sMethod;

    static {
        try {
            sClass = Class.forName(SURFACE_CONTROL);
            sMethod = sClass.getDeclaredMethod(METHOD_NAME, params);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void screenshot(int width, int height) {
        File file = new File(Environment.getExternalStorageDirectory() + "/EasyScreenshot/"
                + new Date().getTime() + ".PNG");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            Bitmap bitmap = (Bitmap) sMethod.invoke(null, new Object[]{width, height});
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
