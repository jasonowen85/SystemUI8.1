package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.os.Bundle;
import android.os.RemoteException;

//import com.adayo.proxy.settings.SettingsManager;
//import com.adayo.proxy.settings.contants.SettingsContantsDef;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//import com.adayo.module.servicecenterproxy.ServiceCenterManager;
//import com.adayo.module.servicecenterproxy.Constant;
//import com.adayo.function.IFunctionNotifyResultBase;
//import com.adayo.module.servicecenterproxy.IFunction;
//import com.adayo.module.servicecenterproxy.IFunctionNotifyResult;

import com.adayo.adayosource.AdayoSource;


/**
 * @author jingtingy
 * @desc 亮度控制实现
 * @created 2018/8/12
 */
public class BrightnessControllerImpl implements BrightnessController{
    private static final boolean DEBUG = false;
    private static final String TAG = "BrightnessControllerImpl";


//
//    private SettingsManager mSettingsManager;
//	private ServiceCenterManager mServiceCenterManager;

    // 主亮度范围: 1 - 9
    private final int mMinimumBrightness = 1;
    private final int mMaximumBrightness = 255;

    private Context mContext;



    public BrightnessControllerImpl(Context mContext) {
        this.mContext = mContext;

        //初始化
//        mSettingsManager = SettingsManager.getSettingsManager();
//		mServiceCenterManager = ServiceCenterManager.getInstance();

    }



    @Override
    public void setProgress(int progress) {

        String[] emptyArr = new String[1];
        Log.e(TAG, "setBrightness: sxf progress ="+progress);
//        int i = mSettingsManager.doDb_BacklightControl(mContext,SettingsContantsDef.MODE_SET,
//                new String[]{String.valueOf(progress)}, emptyArr);
//		try {
//			Bundle bundle = new Bundle();
//			bundle.putBoolean("isOpen", true);
//			int i = mServiceCenterManager.invokeFunc(AdayoSource.ADAYO_SOURCE_HOME, AdayoSource.ADAYO_SOURCE_SETTING, "toggleBackLightSwitch", 0L, bundle);
//			 Log.e(TAG, "setBrightness: sxf i1 ="+i);
            Bundle bundle = new Bundle();
			bundle.putInt("no", progress);
//			int i = mServiceCenterManager.invokeFunc(AdayoSource.ADAYO_SOURCE_HOME, AdayoSource.ADAYO_SOURCE_SETTING, "setScreenBrightness", 0L, bundle);
//			 Log.e(TAG, "setBrightness: sxf i ="+i);
//			String value = (String) mServiceCenterManager.syncRequestGet(AdayoSource.ADAYO_SOURCE_HOME,
//                            AdayoSource.ADAYO_SOURCE_SETTING,"setScreenBrightness", 0L, bundle).get("result");
//							 Log.e(TAG, "setBrightness: sxf value ="+value);
//
//        } catch (RemoteException e) {
//              Log.e(TAG, "setBrightness: e ="+e);
//        }
		notifyResult();
    }
	
	
	 /**
	 * 消费者向服务中心注册的方法，生产者可以回调到消费者，异步传递参数。
	 */
	private void notifyResult() {
//		try {
//			Log.i(TAG, " click sxf vr  freq  1008 registerNotifyHandler!!");
//			IFunctionNotifyResultBase iFunctionNotifyResultBase = new IFunctionNotifyResultBase();
//			iFunctionNotifyResultBase.setFunctionNotifyResult(new IFunctionNotifyResult() {
//				@Override
//				public int callbackResult(String fromModuleName ,String function, long timeStump, Bundle bundle) {
//					int result = bundle.getInt("result");
//					Log.i(TAG,  " sxf callback  result =====  " + result);
//					return 0;
//				}
//			});
//			mServiceCenterManager.registerNotifyHandler(AdayoSource.ADAYO_SOURCE_HOME,iFunctionNotifyResultBase);
//
//		} catch (RemoteException e) {
//			e.printStackTrace();
//			Log.i(TAG, "  registerNotifyHandler!!+e"+e);
//		}
        Log.i(TAG, "  registerNotifyHandler!!+e"+0);
	}

    @Override
    public int getMax() {
        return mMaximumBrightness;
    }


    @Override
    public int getMin() {
        return mMinimumBrightness;
    }

    @Override
    public int getProgress() {
        int progress = 1;
//        String[] out = new String[1];
//        String[] emptyArr = new String[1];
//        int i = mSettingsManager.doDb_BacklightControl(mContext,SettingsContantsDef.MODE_GET,
//                emptyArr, out);
//        Log.e(TAG, "getBrightness: sxf i ="+i+" formatIntData(out[0], 7) ="+formatIntData(out[0], 7));
//        progress = formatIntData(out[0], 0);
        return progress;
    }


//    //类型转换
//    public int formatIntData(String data, int defValue){
//        try{
//            return Integer.parseInt(data);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return defValue;
//    }
//
 }






