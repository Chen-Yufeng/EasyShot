package com.ifchan.easyshot.view.impl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ifchan.easyshot.R;
import com.ifchan.easyshot.util.BitmapUtil;
import com.ifchan.easyshot.util.ScreenshotUtil;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivityImpl extends AppCompatActivity implements EasyPermissions
        .PermissionCallbacks, ScreenshotUtil.Callback{
    private final String TAG = "@SettingActivityImpl";
    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CODE = 1001;
    private MediaProjectionManager mMediaProjectionManager;
    public static final int MEDIA_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        test();
    }

    private void test() {
        ScreenshotUtil screenshotUtil = ScreenshotUtil.INSTANCE;
        screenshotUtil.init(this);
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            requestMediaPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MEDIA_REQUEST_CODE:
                if (resultCode != RESULT_OK) {
                    Log.d(TAG, "User canceled");
                }
                Log.d(TAG, "Start.");
                ScreenshotUtil.INSTANCE.doingScreenCapturing(resultCode, data, this);
                break;
        }
    }

    private void requestPermission() {
        if (!EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            EasyPermissions.requestPermissions(this, "Need write sdcard permission to save " +
                    "screenshot.", REQUEST_CODE, PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        requestMediaPermission();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Need permission to run this app", Toast.LENGTH_LONG).show();
    }

    private void requestMediaPermission() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context
                .MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(),
                MEDIA_REQUEST_CODE);
    }

    @Override
    public void OnScreenShotComplete(Bitmap bitmap) {
        BitmapUtil.saveBitmapToFile(bitmap, 100);
    }
}
