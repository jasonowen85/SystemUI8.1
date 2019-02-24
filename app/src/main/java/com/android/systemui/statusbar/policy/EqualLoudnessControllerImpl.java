package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.util.Log;

//import com.adayo.proxy.audio.AudioDspManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author jingtingy
 * @desc 等响度开关控制实现
 * @created 2018/8/2
 */
public class EqualLoudnessControllerImpl implements EqualLoudnessController {
    private static final String TAG = "LoudnessControllerImpl";
    //Loudness Mode
    private static final int MODE_CLOSE_LOUDNESS = 0;//关闭Loudness
    private static final int MODE_OPEN_LOUDNESS = 1;//打开Loudness

    private Context mContext;
//    private AudioDspManager mAudioDspManager;
    private final ArrayList<EqualLoudnessCallback> mListeners = new ArrayList<>(1);

    public EqualLoudnessControllerImpl(Context mContext) {
        this.mContext = mContext;

        //初始化audio dsp
//        mAudioDspManager = AudioDspManager.getShareDataManager();
//        mAudioDspManager.init();
    }

    @Override
    public boolean isLoudnessModeOpen() {
        //判断等响度开关是否打开
//        if(null != mAudioDspManager){
//            int[] loudnessMode = mAudioDspManager.getLoudnessMode();
//            return null != loudnessMode && loudnessMode[0] == MODE_OPEN_LOUDNESS;
//        }
        return false;
    }

    @Override
    public void openLoudnessMode() {
        //打开等响度开关
//        if(null != mAudioDspManager){
//            mAudioDspManager.setLoudnessMode(MODE_OPEN_LOUDNESS);
//            fireLoudnessCallback(true);
//        }
    }

    @Override
    public void closeLoudnessMode() {
        //关闭等响度开关
//        if(null != mAudioDspManager){
//            mAudioDspManager.setLoudnessMode(MODE_CLOSE_LOUDNESS);
//            fireLoudnessCallback(false);
//        }
    }

    @Override
    public void onLoudnessBtClicked() {
//        if(null != mAudioDspManager){
//            if(isLoudnessModeOpen()){
//                closeLoudnessMode();
//            }else {
//                openLoudnessMode();
//            }
//        }
    }

    @Override
    public void addCallback(EqualLoudnessCallback listener) {
        synchronized (mListeners) {
            cleanUpListenersLocked(listener);
            mListeners.add(listener);
            listener.onLoudnessModeChanged(isLoudnessModeOpen());
        }
    }

    @Override
    public void removeCallback(EqualLoudnessCallback listener) {
        synchronized (mListeners) {
            cleanUpListenersLocked(listener);
        }
    }

    private void cleanUpListenersLocked(EqualLoudnessCallback listener) {
        for (int i = mListeners.size() - 1; i >= 0; i--) {
            EqualLoudnessCallback found = mListeners.get(i);
            if (found == null || found == listener) {
                mListeners.remove(i);
            }
        }
    }

    private void fireLoudnessCallback(boolean isOpen) {
        synchronized (mListeners) {
            for (EqualLoudnessCallback callback : mListeners) {
                callback.onLoudnessModeChanged(isOpen);
            }
        }
    }
}
