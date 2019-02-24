package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

//import com.adayo.proxy.audio.AudioDspManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author jingtingy
 * @desc 系统静音
 * @created 2018/8/2
 */
public class MuteControllerImpl implements MuteController {
    private static final String TAG = "MuteControllerImpl";

    //mute状态
    private static final int CANCEL_MUTE = 0;//取消mute
    private static final int MUTE_ALL = 1;//mute所有通道
    private static final int MUTE_EXCEPT_NAVIGATION = 2;//mute除了导航外的其他通道

    private Context mContext;
//    private AudioDspManager mAudioDspManager;

    private final H mHandler = new H(Looper.getMainLooper());

    public MuteControllerImpl(Context mContext) {
        this.mContext = mContext;

        //初始化audio dsp
//        mAudioDspManager = AudioDspManager.getShareDataManager();
//        mAudioDspManager.init();
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
//                mHandler.sendEmptyMessage(H.MSG_MUTE_STATE_CHANGED);
//            }
//        } else {
//            Log.d(TAG, "mAudioDspManager == null");
//        }
    }

    @Override
    public void cancelSystemMute() {
        //取消静音
//        if (null != mAudioDspManager) {
//            mAudioDspManager.setMuteMode(CANCEL_MUTE);
//        }
    }

    @Override
    public void addCallback(MuteStateChangeCallback listener) {
        mHandler.obtainMessage(H.MSG_ADD_CALLBACK, listener).sendToTarget();
        mHandler.sendEmptyMessage(H.MSG_MUTE_STATE_CHANGED);
    }

    @Override
    public void removeCallback(MuteStateChangeCallback listener) {
        mHandler.obtainMessage(H.MSG_REMOVE_CALLBACK, listener).sendToTarget();
    }

    private void cleanUpListenersLocked(ArrayList<MuteController.MuteStateChangeCallback> callbacks,MuteStateChangeCallback listener) {
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            MuteStateChangeCallback found = callbacks.get(i);
            if (found == null || found == listener) {
                callbacks.remove(i);
            }
        }
    }

    private void fireMuteCallback(ArrayList<MuteController.MuteStateChangeCallback> callbacks,boolean isMuted) {
        synchronized (callbacks) {
            for (MuteStateChangeCallback callback : callbacks) {
                callback.onMuteStateChanged(isMuted);
            }
        }
    }

    private final class H extends Handler {
        private final ArrayList<MuteController.MuteStateChangeCallback> mCallbacks = new ArrayList<>();

        private static final int MSG_MUTE_STATE_CHANGED = 1;
        private static final int MSG_ADD_CALLBACK = 2;
        private static final int MSG_REMOVE_CALLBACK = 3;

        public H(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MUTE_STATE_CHANGED:
                    fireMuteCallback(mCallbacks,isSystemMuted());
                    break;
                case MSG_ADD_CALLBACK: {
                    synchronized (mCallbacks) {
                        cleanUpListenersLocked(mCallbacks,(MuteController.MuteStateChangeCallback) msg.obj);
                        mCallbacks.add((MuteController.MuteStateChangeCallback) msg.obj);
                    }
                }
                break;
                case MSG_REMOVE_CALLBACK: {
                    synchronized (mCallbacks) {
                        cleanUpListenersLocked(mCallbacks,(MuteController.MuteStateChangeCallback) msg.obj);
                    }
                }
                break;
            }
        }
    }
}
