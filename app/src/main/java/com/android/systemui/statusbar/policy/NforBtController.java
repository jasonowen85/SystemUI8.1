package com.android.systemui.statusbar.policy;

//import com.nforetek.bt.aidl.NfHfpClientCall;

/**
 * @author jingtingy
 * @desc
 * @created 2018/12/5
 */
public interface NforBtController extends CallbackController<NforBtController.NforBtStateChangeCallback>{

    boolean isBluetoothEnabled();

    boolean isBluetoothConnecting();

    int getBluetoothState();

    boolean isBluetoothConnected();

    void answerThePhone();

    void hangUpThePhone();

    public interface NforBtStateChangeCallback{
        default void onNforBtStateChanged(boolean conneted){}
//        default void onHfpCallChanged(String callName,NfHfpClientCall clientCall){}
        default void onHfpCallingTimeChanged(String time){}
    }
}
