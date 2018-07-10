package com.ifchan.easyshot.ui.impl;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ifchan.easyshot.BaseActivity;
import com.ifchan.easyshot.R;
import com.ifchan.easyshot.presenter.Impl.ScreenshotPresenterImpl;
import com.ifchan.easyshot.presenter.ScreenshotPresenter;
import com.ifchan.easyshot.ui.SettingActivity;
import com.ifchan.easyshot.util.ScreenshotManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivityImpl extends AppCompatActivity implements SettingActivity,
        BaseActivity, EasyPermissions.PermissionCallbacks {
    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CODE = 1001;
    private ScreenshotPresenter mScreenshotPresenter;

    @OnClick(R.id.test)
    void onButtonClicked() {
        mScreenshotPresenter.takeScreenshot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        requestPermission();
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mScreenshotPresenter = new ScreenshotPresenterImpl(this);
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

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Need permission to run this app", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter() {
        mScreenshotPresenter = new ScreenshotPresenterImpl(this);
    }

    @Override
    public void showResult(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
