package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.base.jar.NforeBtBaseJar;

import java.util.ArrayList;

/**
 * @author jingtingy
 * @desc
 * @created 2018/12/5
 */
public class NforBtControllerImpl implements NforBtController
        ,NforeBtBaseJar.BluetoothSettingChangeListener
,NforeBtBaseJar.BluetoothServiceConnectedListener,NforeBtBaseJar.BluetoothPhoneChangeListener{
    public static final String TAG = "NforBtControllerImpl";
    public static final boolean DEBUG = false;

    private Context mContext;
    private boolean isInitSuccess;
    private boolean isNforBtRegisted;
    private NfHfpClientCall mCurrentCallClient;
    private String mCurrentCallName;
    private String mCallTime;

    private final H mHandler = new H(Looper.getMainLooper());

    public NforBtControllerImpl(Context mContext) {
        this.mContext = mContext;
        registNforBtListener();
    }

    private void registNforBtListener(){
        if(!isInitSuccess){
            NforeBtBaseJar.init(mContext);
            isInitSuccess = true;
        }

        if(!isNforBtRegisted){
            NforeBtBaseJar.registerBluetoothServiceConnectedListener(this);
            NforeBtBaseJar.registerBluetoothSettingChangeListener(this);
            NforeBtBaseJar.registerBluetoothPhoneChangeListener(this);
            isNforBtRegisted = true;
        }
    }

    //----------- NforBtController start -----------------------------------------------------------
    @Override
    public boolean isBluetoothEnabled() {
        boolean enable  = false;
        try {
            enable = NforeBtBaseJar.isBtEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"NforBt isBluetoothEnabled ==> enable: " + enable);
        return enable;
    }

    @Override
    public boolean isBluetoothConnecting() {
        return false;
    }

    @Override
    public int getBluetoothState() {
        boolean conneted = NforeBtBaseJar.isBtConnected();
        Log.d(TAG,"NforBt getBluetoothState ==> connected: " + conneted);
        return 0;
    }

    @Override
    public boolean isBluetoothConnected() {
        boolean conneted = NforeBtBaseJar.isBtConnected();
        Log.d(TAG,"NforBt isBluetoothConnected ==> connected: " + conneted);
        return conneted;
    }

    @Override
    public void answerThePhone() {
        Log.d(TAG,"NforBt answerThePhone accept" );
        NforeBtBaseJar.accept();
    }

    @Override
    public void hangUpThePhone() {
        Log.d(TAG,"NforBt hangUpThePhone" );
        NforeBtBaseJar.hangUp();
    }

    @Override
    public void addCallback(NforBtStateChangeCallback listener) {
        mHandler.obtainMessage(H.MSG_ADD_CALLBACK, listener).sendToTarget();
        mHandler.sendEmptyMessage(H.MSG_UPDATE_BT_STATE);
    }

    @Override
    public void removeCallback(NforBtStateChangeCallback listener) {
        mHandler.obtainMessage(H.MSG_REMOVE_CALLBACK, listener).sendToTarget();
    }

    public void releaseNforBt(){
        NforeBtBaseJar.release();
        isNforBtRegisted = false;
        isInitSuccess = false;
    }
    //----------- NforBtController end -----------------------------------------------------------

    //---------- NforeBtBaseJar.BluetoothSettingChangeListener start -------------------------------
    @Override
    public void onEnableChanged(boolean b) {
        Log.d(TAG,"NforBt onEnableChanged ==> isConnected: " + b);
        mHandler.sendEmptyMessage(H.MSG_UPDATE_BT_STATE);
    }

    @Override
    public void onConnectedChanged(String s, int i) {
        Log.d(TAG,"NforBt onConnectedChanged ==> address: " + s + "  isConnected: " + i);
        mHandler.sendEmptyMessage(H.MSG_UPDATE_BT_STATE);
    }

    @Override
    public void onHfpStateChanged(String s, int i) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onHfpAudioStateChanged(String s, int i, int i1) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onAvrcpStateChanged(String s, int i) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onA2dpStateChanged(String s, int i) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onAdapterDiscoveryStarted() {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onAdapterDiscoveryFinished() {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void retPairedDevices(int i, String[] strings, String[] strings1, int[] ints) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onDeviceFound(String s, String s1) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onDeviceBondStateChanged(String s, String s1, int i) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onLocalAdapterNameChanged(String s) {
        Log.d(TAG,"NforBt onHfpStateChanged");
    }

    @Override
    public void onPairStateChanged(String s, String s1, int i, int i1) {

    }
    //---------- NforeBtBaseJar.BluetoothSettingChangeListener end ---------------------------------

    //---------- NforeBtBaseJar.BluetoothPhoneChangeListener start ----------------------------
    @Override
    public void onHfpCallChanged(String s, NfHfpClientCall nfHfpClientCall) {
        Log.d(TAG,"NforBt onHfpCallChanged  s: " + s + "  nfHfpClientCall: " + nfHfpClientCall.toString());
        mCurrentCallClient = nfHfpClientCall;
        if(!TextUtils.isEmpty(nfHfpClientCall.getNumber())){
            try {
                mCurrentCallName = NforeBtBaseJar.getCallName(nfHfpClientCall.getNumber());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mHandler.sendEmptyMessage(H.MSG_UPDATE_PHONE_CALL_STATE);
    }

    @Override
    public void onHfpCallingTimeChanged(String s) {
        Log.d(TAG,"NforBt onHfpCallingTimeChanged s: " + s);
        mCallTime = s;
        mHandler.sendEmptyMessage(H.MSG_UPDATE_PHONE_CALL_TIME);
    }

    @Override
    public void onPbapStateChanged(int i) {
        Log.d(TAG,"NforBt onPbapStateChanged i: " + i);
    }
    //---------- NforeBtBaseJar.BluetoothPhoneChangeListener end ---------------------------------

    //---------- NforeBtBaseJar.BluetoothServiceConnectedListener start ----------------------------
    @Override
    public void onServiceConnectedChanged(boolean b) {
        Log.d(TAG,"NforBt onServiceConnectedChanged ==>   isServiceConnected: " + b);

    }
    //---------- NforeBtBaseJar.BluetoothServiceConnectedListener end ------------------------------

    private void fireNforBtCallback(ArrayList<NforBtStateChangeCallback> callbacks) {
        synchronized (callbacks) {
            for (NforBtStateChangeCallback callback : callbacks) {
                callback.onNforBtStateChanged(isBluetoothConnected());
            }
        }
    }

    private void fireNforBtPhoneCallState(ArrayList<NforBtStateChangeCallback> callbacks) {
        synchronized (callbacks) {
            for (NforBtStateChangeCallback callback : callbacks) {
                //callback.onHfpCallChanged(mCurrentCallName);
            }
        }
    }

    private void fireNforBtPhoneCallTime(ArrayList<NforBtStateChangeCallback> callbacks) {
        synchronized (callbacks) {
            for (NforBtStateChangeCallback callback : callbacks) {
                callback.onHfpCallingTimeChanged(mCallTime);
            }
        }
    }

    private void cleanUpListenersLocked(ArrayList<NforBtStateChangeCallback> callbacks,NforBtStateChangeCallback listener) {
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            NforBtStateChangeCallback found = callbacks.get(i);
            if (found == null || found == listener) {
                callbacks.remove(i);
            }
        }

        if(null != callbacks && callbacks.size() == 0 && isNforBtRegisted){
            NforeBtBaseJar.release();
            isNforBtRegisted = false;
            isInitSuccess = false;
        }
    }

    private final class H extends Handler {
        private final ArrayList<NforBtStateChangeCallback> mCallbacks = new ArrayList<>();
        private static final int MSG_UPDATE_BT_STATE = 1;
        private static final int MSG_ADD_CALLBACK = 2;
        private static final int MSG_REMOVE_CALLBACK = 3;
        private static final int MSG_UPDATE_PHONE_CALL_STATE = 4;
        private static final int MSG_UPDATE_PHONE_CALL_TIME = 5;

        public H(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_BT_STATE:
                    fireNforBtCallback(mCallbacks);
                    break;
                case MSG_UPDATE_PHONE_CALL_STATE:
                    fireNforBtPhoneCallState(mCallbacks);
                    break;
                case MSG_UPDATE_PHONE_CALL_TIME:
                    fireNforBtPhoneCallTime(mCallbacks);
                    break;
                case MSG_ADD_CALLBACK: {
                    synchronized (mCallbacks) {
                        //cleanUpListenersLocked(mCallbacks,(NforBtController.NforBtStateChangeCallback) msg.obj);
                        mCallbacks.add((NforBtStateChangeCallback) msg.obj);
                        //registNforBtListener();
                    }
                }
                break;
                case MSG_REMOVE_CALLBACK: {
                    synchronized (mCallbacks) {
                        //cleanUpListenersLocked(mCallbacks,(NforBtController.NforBtStateChangeCallback) msg.obj);
                        mCallbacks.remove((NforBtStateChangeCallback) msg.obj);
                    }
                }
                break;
            }
        }
    }
}
