package com.android.systemui.statusbar.policy;

import android.content.Context;

//import com.adayo.proxy.settings.SettingExternalManager;

import java.util.ArrayList;

/**
 * @author jingtingy
 * @desc 背光控制开关
 * @created 2018/11/23
 */
public class BrightnessSwitchControllerImpl implements BrightnessSwitchController{
    public static final String TAG = "ScreenOffControllerImpl";
    public static final boolean MY_DEBUG = false;

    //当前屏幕状态 Display
    /** 关屏按钮不可用(异常:systemService未connected或者系统状态为非正常开机状态)*/
    public static final int SCREEN_STATE_UNAVAILABLE = 0;
    /** 亮屏状态*/
    public static final int SCREEN_DISPLAY_ON = 1;
    /** 关屏状态*/
    public static final int SCREEN_DISPLAY_OFF = 2;

    private Context mContext;
//    private SettingExternalManager mSettingExternalManager;
    private ArrayList<BrightnessSwitchController.BrightnessChangeCallback> mCallbacks = new ArrayList<>();

    public BrightnessSwitchControllerImpl(Context mContext) {
        this.mContext = mContext;
//        mSettingExternalManager = SettingExternalManager.getSettingsManager();
    }

    @Override
    public void requestDisplayOff() {
//        if(null != mSettingExternalManager){
//            //设置关屏
//            mSettingExternalManager.toggleBackLightSwitch(false);
//        }
    }

    @Override
    public int getDisplayStatus() {
        return 1;
    }

    @Override
    public void addCallback(BrightnessChangeCallback listener) {
        synchronized (mCallbacks) {
            cleanUpListenersLocked(listener);
            mCallbacks.add(listener);
            listener.onBrightnessChanged(true);
        }
    }

    @Override
    public void removeCallback(BrightnessChangeCallback listener) {
        synchronized (mCallbacks) {
            cleanUpListenersLocked(listener);
        }
    }

    private void cleanUpListenersLocked(BrightnessChangeCallback listener) {
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
            BrightnessChangeCallback found = mCallbacks.get(i);
            if (found == null || found == listener) {
                mCallbacks.remove(i);
            }
        }
    }
}
