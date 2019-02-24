package com.android.systemui.statusbar.policy;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;
//
//import com.adayo.systemserviceproxy.ISystemServiceCallback;
//import com.adayo.systemserviceproxy.SystemServiceManager;

import java.util.ArrayList;


/**
 * @author jingtingy
 * @desc 关屏
 * @created 2018/7/16
 */
public class ScreenOffControllerImpl implements ScreenOffController{//,ISystemServiceCallback
    public static final String TAG = "ScreenOffControllerImpl";
    public static final boolean MY_DEBUG = false;

    /** 监听模块id */
    private static final byte FUNCTION_ID = 0x08;

    //系统状态
    /** 初始化状态，收到此状态可不做处理 */
    private static final byte SYSTEM_STATUS_INITING = 0x00;
    /**  预启动状态，声音停止，屏幕关闭，不响应用户操作，用户Acc on唤醒*/
    private static final byte SYSTEM_STATUS_PRESTART = 0x01;
    /**  假关机状态，声音停止，屏幕关闭，不响应用户操作，长按power键唤醒或者用户Acc on*/
    private static final byte SYSTEM_STATUS_FAKE_SHUT_DOWN = 0x02;
    /**  假关机中，收到此状态可不做处理*/
    private static final byte SYSTEM_STATUS_FAKE_SHUT_DOWNINT = 0x03;
    /**  正常开机，声音、屏幕、动作正常*/
    private static final byte SYSTEM_STATUS_NORMAL = 0x04;
    /**  待机状态，声音停止、屏幕显示待机时钟或者黑屏。响应倒车、空调、语言唤醒等操作*/
    private static final byte SYSTEM_STATUS_POWER_OFF = 0x05;
    /**  升级状态，声音停止不响应用户操作*/
    private static final byte SYSTEM_STATUS_UPDATE = 0x06;
    /**  准备状态，srcMng申请视频控制权，Camera申请倒车权，常时service给systemservice回复start OK*/
    private static final byte SYSTEM_STATUS_READY = 0x07;

    //当前屏幕状态 Display
    /** 关屏按钮不可用(异常:systemService未connected或者系统状态为非正常开机状态)*/
    public static final int SCREEN_STATE_UNAVAILABLE = 0;
    /** 亮屏状态*/
    public static final int SCREEN_DISPLAY_ON = 1;
    /** 关屏状态*/
    public static final int SCREEN_DISPLAY_OFF = 2;

    /** 模块启动OK*/
    public static final char SYSTEMUI_START_OK = 0x80;
    /** 假关机OK*/
    public static final char SYSTEMUI_SHUT_DOWN_OK = 0x81;

    private Context mContext;
//    private SystemServiceManager mSystemServiceManager;
    //当前系统状态
    private byte mCurrentSystemStatus;
    private boolean isSystemServiceConnected;
    private boolean isSystemServiceRegisted;
    private final ArrayList<ScreenStateChangeCallback> mCallbacks = new ArrayList<>(1);

    public ScreenOffControllerImpl(Context mContext) {
        this.mContext = mContext;

//        mSystemServiceManager = SystemServiceManager.getInstance();

        //要调用systemService接口，必须是基于获取systemService为true的前提
//        if(mSystemServiceManager.conectsystemService()){
//            isSystemServiceConnected = true;
//            if(MY_DEBUG)Log.d(TAG,"registSystemServiceCallback FUNCTION_ID: " + FUNCTION_ID);
//            mSystemServiceManager.registSystemServiceCallback(this,FUNCTION_ID);
//            isSystemServiceRegisted = true;
//        }
        if(MY_DEBUG)Log.d(TAG,"isSystemServiceConnected = " + isSystemServiceConnected);
    }

    @Override
    public boolean isSystemServiceConnected() {
        if(!isSystemServiceConnected){
//            isSystemServiceRegisted = mSystemServiceManager.conectsystemService();
            registSystemServiceCallback();
        }
        if(MY_DEBUG)Log.d(TAG,"isSystemServiceConnected = " + isSystemServiceConnected);
        if(!isSystemServiceConnected){
            if(MY_DEBUG)Log.d(TAG,"systemService连接失败!");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void registSystemServiceCallback() {
        if(isSystemServiceConnected && !isSystemServiceRegisted){
            if(MY_DEBUG)Log.d(TAG,"registSystemServiceCallback FUNCTION_ID: " + FUNCTION_ID);
//            mSystemServiceManager.registSystemServiceCallback(this,FUNCTION_ID);
            isSystemServiceRegisted = true;
        }
    }

    @Override
    public void unregistSystemServiceCallback() {
        if(isSystemServiceConnected && isSystemServiceRegisted){
            if(MY_DEBUG)Log.d(TAG,"unregistSystemServiceCallback FUNCTION_ID: " + FUNCTION_ID);
//            mSystemServiceManager.unregistSystemServiceCallback(this,FUNCTION_ID);
            isSystemServiceRegisted = false;
        }
    }

    @Override
    public void requestDisplayOff() {
//        if(null == mSystemServiceManager || !isSystemServiceConnected())return;
        //正常开机&&屏幕正常
//        if(mSystemServiceManager.getSystemStatus() == SYSTEM_STATUS_NORMAL && mSystemServiceManager.getDisplayStatus()){
//            mSystemServiceManager.requestDisplayOff();
//        }
    }

    @Override
    public int getDisplayStatus() {
//        if(MY_DEBUG)Log.d(TAG,"getDisplayStatus");
//        if(null == mSystemServiceManager
//                || !isSystemServiceConnected()
//                || mSystemServiceManager.getSystemStatus() != SYSTEM_STATUS_NORMAL){
//            if(MY_DEBUG)Log.d(TAG,"getSystemStatus = " + mSystemServiceManager.getSystemStatus());
//            return SCREEN_STATE_UNAVAILABLE;
//        }
//        if(mSystemServiceManager.getDisplayStatus()){
//            if(MY_DEBUG)Log.d(TAG,"getDisplayStatus = SCREEN_DISPLAY_ON");
//            return SCREEN_DISPLAY_ON;
//        }else {
//            if(MY_DEBUG)Log.d(TAG,"getDisplayStatus = SCREEN_DISPLAY_OFF");
//            return SCREEN_DISPLAY_OFF;
//        }
        return 0;
    }

    @Override
    public void addCallback(ScreenStateChangeCallback listener) {
        if(MY_DEBUG)Log.d(TAG,"addCallback");
        synchronized (mCallbacks) {
            cleanUpListenersLocked(listener);
            registSystemServiceCallback();
            mCallbacks.add(listener);
            listener.onScreenStateChanged(getDisplayStatus());
        }
    }

    @Override
    public void removeCallback(ScreenStateChangeCallback listener) {
        if(MY_DEBUG)Log.d(TAG,"removeCallback");
        synchronized (mCallbacks) {
            cleanUpListenersLocked(listener);
        }
    }

    private void cleanUpListenersLocked(ScreenStateChangeCallback listener) {
        for (int i = mCallbacks.size() - 1; i >= 0; i--) {
            ScreenStateChangeCallback found = mCallbacks.get(i);
            if (found == null || found == listener) {
                mCallbacks.remove(i);
            }
        }

//        if(null != mSystemServiceManager && mCallbacks.size() == 0){
//            unregistSystemServiceCallback();
//        }
    }

    private void fireScreenStateCallback() {
        if(MY_DEBUG)Log.d(TAG,"fireScreenStateCallback");
        synchronized (mCallbacks) {
            int state = getDisplayStatus();
            for (ScreenStateChangeCallback callback : mCallbacks) {
                callback.onScreenStateChanged(state);
            }
        }
    }

    /**
     * ISystemServiceCallback:Framework应用层要求实现接口类，通过注册机制实现systemService端对Framework层service/APP模块的状态通知
     *
     * 处理systemService通知状态变更
     * @param b 执行是否成功 true:成功 false:失败
     */
//    @Override
//    public void systemStatusNotify(byte b) {
//        if(MY_DEBUG)Log.d(TAG,"===> systemStatusNotify 状态: " + b);
//        mCurrentSystemStatus = b;
//        switch (mCurrentSystemStatus){
//            case SYSTEM_STATUS_NORMAL:{
//                fireScreenStateCallback();
//            }break;
//            case SYSTEM_STATUS_FAKE_SHUT_DOWN:{
//                //假关机
//                if(MY_DEBUG)Log.d(TAG,"收到 shutdown 状态");
////                if(null != mSystemServiceManager){
////                    mSystemServiceManager.responsStatus((byte)SYSTEMUI_SHUT_DOWN_OK,FUNCTION_ID);
////                }
//            }break;
//            case SYSTEM_STATUS_READY:{
//                //ready 状态
//                if(MY_DEBUG)Log.d(TAG,"收到 ready 状态");
////                if(null != mSystemServiceManager){
//////                    mSystemServiceManager.responsStatus((byte)SYSTEMUI_START_OK,FUNCTION_ID);
////                }
//            }break;
//            default:
//                break;
//        }
//    }
}
