package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

//import com.adayo.proxy.audio.AudioDspManager;
import com.adayo.proxy.share.ShareDataManager;
import com.adayo.proxy.share.interfaces.IShareDataListener;
import com.android.systemui.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author jingtingy
 * @desc 音量控制实现
 * @created 2018/8/12
 */
public class VolumeControllerImpl implements VolumeController,IShareDataListener{
    private static final boolean DEBUG = false;
    private static final String TAG = "VolumeControllerImpl";
    //mute状态
    private static final int CANCEL_MUTE = 0;//取消mute
    private static final int MUTE_ALL = 1;//mute所有通道
    private static final int MUTE_EXCEPT_NAVIGATION = 2;//mute除了导航外的其他通道

    private final int SHARE_DATA_ID = 7;

    // 主音量范围: 0 - 40
    private final int mMinimumVolume = 0;
    private final int mMaximumVolume = 40;

    private Context mContext;
//    private AudioDspManager mAudioDspManager;
    private ShareDataManager mShareDataManager;
    private boolean isServiceConnected;
    private boolean isServiceRegisted;
    private int mBootVolumeValue;
    private final String VOLUME_KEY = "systemui_volume_key";

    private final H mHandler = new H(Looper.getMainLooper());

    public VolumeControllerImpl(Context mContext) {
        this.mContext = mContext;

        //初始化audio dsp
//        mAudioDspManager = AudioDspManager.getShareDataManager();
//        mAudioDspManager.init();

//        mShareDataManager = ShareDataManager.getShareDataManager();
        registerShareData();
    }

    private void registerShareData(){
        if(null == mShareDataManager)return;
//        if(!isServiceConnected){
//            isServiceConnected = mShareDataManager.getServiceConnection();
//        }
//        if(isServiceConnected && !isServiceRegisted){
//            mShareDataManager.registerShareDataListener(SHARE_DATA_ID,this);
//            isServiceRegisted = true;
//
//            initBootVolumeValue(mShareDataManager.getShareData(SHARE_DATA_ID));
//        }
        if(DEBUG)Log.d(TAG,"===> isServiceConnected: " + isServiceConnected + "  isServiceRegisted: " + isServiceRegisted);
    }

    private void unRegisterShareData(){
        if(null == mShareDataManager)return;
//        if(isServiceConnected && isServiceRegisted){
//            mShareDataManager.unregisterShareDataListener(SHARE_DATA_ID,this);
//            isServiceRegisted = false;
//        }
    }

    @Override
    public boolean isSystemMuted() {
//        if (null != mAudioDspManager) {
//            //获取静音状态
//            int[] muteMode = mAudioDspManager.getMuteMode();
//            return null != muteMode && muteMode[0] == MUTE_ALL;
//        }

        return false;
    }

    @Override
    public void setSystemMute() {
        //设置静音，mute所有通道
//        if (null != mAudioDspManager) {
//            //获取静音状态
//            int[] muteMode = mAudioDspManager.getMuteMode();
//            if (null != muteMode) {
//                switch (muteMode[0]) {
//                    case CANCEL_MUTE: {
//                        //所有通道静音
//                        mAudioDspManager.setMuteMode(MUTE_ALL);
//                    }
//                    break;
//                    case MUTE_ALL: {
//                        //取消静音
//                        mAudioDspManager.setMuteMode(CANCEL_MUTE);
//                    }
//                    break;
//                    case MUTE_EXCEPT_NAVIGATION: {
//                        //所有通道静音
//                        mAudioDspManager.setMuteMode(MUTE_ALL);
//                    }
//                    break;
//                }
//                mHandler.sendEmptyMessage(H.MSG_UPDATE_VOLUME_STATE);
//            }
//        } else {
//            Log.d(TAG, "mAudioDspManager == null");
//        }
    }

    @Override
    public void setProgress(int progress) {
//        if(null != mAudioDspManager){
//            mAudioDspManager.setMainVolume(progress);
//			mAudioDspManager.setNaviVolume(progress);
//            if(progress == 0){
//                mAudioDspManager.setMuteMode(MUTE_ALL);
//            }else {
//                mAudioDspManager.setMuteMode(CANCEL_MUTE);
//            }
//            SPUtils.put(mContext,VOLUME_KEY,progress);
//            mHandler.sendEmptyMessage(H.MSG_UPDATE_VOLUME_STATE);
//        }
    }

    @Override
    public int getMax() {
        return mMaximumVolume;
    }

    @Override
    public int getProgress() {
        if(isSystemMuted())return 0;
        int progress = 0;
//        if(null != mAudioDspManager){
//            int[] value = mAudioDspManager.getMainVolume();
//            if(null != value){
//                progress = value[0];
//            }
//            if(progress == 0){
//                progress = mBootVolumeValue;
//            }
//        }
        return progress;
    }

    private void fireVolumeCallback(ArrayList<VolumeController.VolumeChangeCallback> callbacks) {
        synchronized (callbacks) {
            boolean isMute = isSystemMuted();
            int progress = getProgress();
            for (VolumeChangeCallback callback : callbacks) {
                callback.onVolumeStateChanged(isMute,progress);
            }
        }
    }

    private void cleanUpListenersLocked(ArrayList<VolumeController.VolumeChangeCallback> callbacks,VolumeChangeCallback listener) {
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            VolumeChangeCallback found = callbacks.get(i);
            if (found == null || found == listener) {
                callbacks.remove(i);
            }
        }
    }

    @Override
    public void addCallback(VolumeChangeCallback listener) {
        mHandler.obtainMessage(H.MSG_ADD_CALLBACK, listener).sendToTarget();
        mHandler.sendEmptyMessage(H.MSG_UPDATE_VOLUME_STATE);
    }

    @Override
    public void removeCallback(VolumeChangeCallback listener) {
        mHandler.obtainMessage(H.MSG_REMOVE_CALLBACK, listener).sendToTarget();
    }

    @Override
    public void notifyShareData(int i, String s) {
        if(DEBUG)Log.d(TAG,"start notifyShareData");
        if(i == SHARE_DATA_ID){
            initBootVolumeValue(s);
            setBootVolumeValue();
        }
        if(DEBUG)Log.d(TAG,"end notifyShareData");
    }

    private final class H extends Handler {
        private final ArrayList<VolumeController.VolumeChangeCallback> mCallbacks = new ArrayList<>();

        private static final int MSG_UPDATE_VOLUME_STATE = 1;
        private static final int MSG_ADD_CALLBACK = 2;
        private static final int MSG_REMOVE_CALLBACK = 3;

        public H(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_VOLUME_STATE:
                    fireVolumeCallback(mCallbacks);
                    break;
                case MSG_ADD_CALLBACK: {
                    synchronized (mCallbacks) {
                        cleanUpListenersLocked(mCallbacks,(VolumeController.VolumeChangeCallback) msg.obj);
                        mCallbacks.add((VolumeController.VolumeChangeCallback) msg.obj);
                        registerShareData();
                    }
                }
                break;
                case MSG_REMOVE_CALLBACK: {
                    synchronized (mCallbacks) {
                        cleanUpListenersLocked(mCallbacks,(VolumeController.VolumeChangeCallback) msg.obj);
                        unRegisterShareData();
                    }
                }
                break;
            }
        }
    }

    /**
     * 解析开机音量
     * @param shareData 音量有关的json字符串
     */
    private void initBootVolumeValue(String shareData){
        if(DEBUG)Log.d(TAG,"start initBootVolumeValue");
        if(null != mShareDataManager){
            if(!TextUtils.isEmpty(shareData)){
                //解析开机音量
                if(DEBUG)Log.d(TAG,"===> shareData: " + shareData);
                try {
                    JSONObject object = new JSONObject(shareData);
                    //开机音量开关
                    boolean switchState = false;
                    if(object.has("sys_on_volume_switch")){
                        switchState = object.getBoolean("sys_on_volume_switch");
                    }
                    if(DEBUG)Log.d(TAG,"sys_on_volume_switch : " + switchState);

                    if(switchState && object.has("sys_on_volume")){
                        mBootVolumeValue = object.getInt("sys_on_volume");
                    }else {
                        mBootVolumeValue = SPUtils.getInt(mContext,VOLUME_KEY,0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(DEBUG)Log.d(TAG,"===> mBootVolumeValue: " + mBootVolumeValue);
    }

    /**
     * 设置开机音量
     */
    private void setBootVolumeValue(){
//        if(null != mAudioDspManager){
//            mAudioDspManager.setMainVolume(mBootVolumeValue);
//            mAudioDspManager.setNaviVolume(mBootVolumeValue);
//            if(mBootVolumeValue == 0){
//                mAudioDspManager.setMuteMode(MUTE_ALL);
//            }else {
//                mAudioDspManager.setMuteMode(CANCEL_MUTE);
//            }
//            SPUtils.put(mContext,VOLUME_KEY,mBootVolumeValue);
//            mHandler.sendEmptyMessage(H.MSG_UPDATE_VOLUME_STATE);
//        }
    }
}
