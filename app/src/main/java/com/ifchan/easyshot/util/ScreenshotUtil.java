package com.ifchan.easyshot.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.nio.ByteBuffer;

public enum ScreenshotUtil {
    INSTANCE;
    private Context mContext;
    private boolean doingScreenCapturing = false;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;
    private WindowManager mWindowManager;
    private int width;
    private int height;
    private int DPI;
    public static final String NAME = "ScreenShot";

    public void init(Context context) {
        mContext = context;
        mMediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context
                .MEDIA_PROJECTION_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DPI = getDPI();
        width = getScreenSize().x;
        height = getScreenSize().y;
    }

    public void doingScreenCapturing(int resultCode, Intent resultData, Callback callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode,
                        resultData);
                mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
                mVirtualDisplay = mMediaProjection.createVirtualDisplay(NAME, width,
                        height, DPI, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, null);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Image image = mImageReader.acquireLatestImage();
                int width = image.getWidth();
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                        Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                image.close();
                callback.OnScreenShotComplete(bitmap);
            }
        }.start();
    }

    private Point getScreenSize() {
        Display display = mWindowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    private int getDPI() {
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    public interface Callback {
        void OnScreenShotComplete(Bitmap bitmap);
    }
}
