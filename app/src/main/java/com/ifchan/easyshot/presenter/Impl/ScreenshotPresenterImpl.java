package com.ifchan.easyshot.presenter.Impl;

import android.graphics.Bitmap;

import com.ifchan.easyshot.presenter.ScreenshotPresenter;
import com.ifchan.easyshot.ui.SettingActivity;
import com.ifchan.easyshot.util.ScreenshotManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScreenshotPresenterImpl implements ScreenshotPresenter {
    private SettingActivity mSettingActivity;

    public ScreenshotPresenterImpl(SettingActivity settingActivity) {
        mSettingActivity = settingActivity;
    }

    @Override
    public void takeScreenshot() {
        ScreenshotManager.screenshot(1080, 1920);
    }
}
