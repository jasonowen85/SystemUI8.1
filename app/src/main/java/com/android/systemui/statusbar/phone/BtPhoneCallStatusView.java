package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adayo.adayosource.AdayoSource;
import com.adayo.proxy.sourcemngproxy.Beans.AppConfigType;
import com.adayo.proxy.sourcemngproxy.Beans.SourceInfo;
import com.adayo.proxy.sourcemngproxy.Control.SrcMngSwitchProxy;
import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.statusbar.policy.NforBtController;
//import com.nforetek.bt.aidl.NfHfpClientCall;

/**
 * @author jingtingy
 * @desc
 * @created 2018/12/24
 */
public class BtPhoneCallStatusView extends LinearLayout implements NforBtController.NforBtStateChangeCallback{
        //,View.OnClickListener
    private final String TAG = "BtPhoneCallStatusView";

    private Context mContext;
    private TextView mCallNameTv;
    private TextView mPhoneNumberTv;
    private TextView mPhoneTimeTv;
    private ImageView mAnswerBt;
    private ImageView mTerminatedBt;
    private NforBtController mNforBtController;
    private OnBtPhoneCallChangedListener mPhoneCallChangedListener;

    private float mDownX;
    private float mDownY;

    public BtPhoneCallStatusView(Context context) {
        this(context,null);
    }

    public BtPhoneCallStatusView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BtPhoneCallStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundResource(R.mipmap.bt_pop_top_bg);
        LayoutInflater.from(context).inflate(R.layout.notification_nfor_bt_phone, this, true);
        mCallNameTv = findViewById(R.id.tv_nfor_bt_call_name);
        mPhoneNumberTv = findViewById(R.id.tv_nfor_bt_phone_number);
        mPhoneTimeTv = findViewById(R.id.tv_nfor_bt_call_time);
        mAnswerBt = findViewById(R.id.answer_phone_bt);
        mTerminatedBt = findViewById(R.id.terminated_phone_bt);
        //mAnswerBt.setOnClickListener(this);
        //mTerminatedBt.setOnClickListener(this);

        mNforBtController = Dependency.get(NforBtController.class);
        addPhoneCallBack();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean consumed = false;
        /*
         * getX()、getY()返回的是触摸点相对于BtPhoneCallStatusView的位置
         * getRawX()、getRawY()返回的是触摸点相对于屏幕的位置
         */
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"===> onTouchEvent ACTION_DOWN");
                mDownX = evX;
                mDownY = evY;
                if(isWithinAnswerButton(evX,evY)){
                    mAnswerBt.setPressed(true);
                }

                if(isWithinHangUpButton(evX,evY)){
                    mTerminatedBt.setPressed(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"===> onTouchEvent ACTION_MOVE");
                if(Math.abs(mDownX - evX) < 10 && Math.abs(mDownY - evY) < 10){
                    consumed = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"===> onTouchEvent ACTION_UP");
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"===> onTouchEvent ACTION_CANCEL");
                if (mAnswerBt.isPressed() && isWithinAnswerButton(evX, evY)) {
                    consumed = true;
                    onAnswerBtClicked();
                }
                mAnswerBt.setPressed(false);

                if (mTerminatedBt.isPressed() && isWithinHangUpButton(evX, evY)) {
                    consumed = true;
                    onTerminatedBtClicked();
                }
                mTerminatedBt.setPressed(false);

                //源管理调起BT通话界面
                Log.d(TAG,"===> onTouchEvent consumed：" + consumed);
                if(!consumed){
                    /*
                    SourceInfo info = new SourceInfo("com.nforetek.btphone",
                            AppConfigType.SourceSwitch.APP_ON.getValue(),
                            AppConfigType.SourceType.UI_AUDIO.getValue());
                    */
                    SourceInfo info = new SourceInfo(AdayoSource.ADAYO_SOURCE_BT_PHONE,
                            AppConfigType.SourceSwitch.APP_ON.getValue(),
                            AppConfigType.SourceType.UI_AUDIO.getValue());

//                    SrcMngSwitchProxy.getInstance().onRequest(info);
                    consumed = true;
                }
                break;
        }

        //return super.onTouchEvent(event);
        return consumed;
    }

    private boolean isWithinAnswerButton(float x, float y) {
        Rect rect = new Rect();
        mAnswerBt.getGlobalVisibleRect(rect);
        boolean within = false;// mAnswerBt.getVisibility() == View.VISIBLE &&mAnswerBt.pointInView(x - rect.left, y - rect.top, 0 /* slop */);
        Log.d(TAG,"===> isWithinAnswerButton  rect: " + rect.toString() + "  within: " + within);
        return within;
    }

    private boolean isWithinHangUpButton(float x, float y) {
        Rect rect = new Rect();
        mTerminatedBt.getGlobalVisibleRect(rect);
        boolean within = false;//mTerminatedBt.getVisibility() == View.VISIBLE &&mTerminatedBt.pointInView(x - rect.left, y - rect.top, 0 /* slop */);
        Log.d(TAG,"===> isWithinHangUpButton  rect: " + rect.toString() + "  within: " + within);
        return within;
    }

    public void addPhoneCallBack(){
        if(null != mNforBtController){
            mNforBtController.addCallback(this);
        }
    }

    public void removeCallBack(){
        if(null != mNforBtController){
            mNforBtController.removeCallback(this);
        }
    }

//    @Override
//    public void onHfpCallChanged(String callName,NfHfpClientCall clientCall) {
//        /*
//          NfHfpClientCall.CALL_STATE_ACTIVE://通话中
//          NfHfpClientCall.CALL_STATE_HELD://等待通话
//          NfHfpClientCall.CALL_STATE_DIALING://去电
//          NfHfpClientCall.CALL_STATE_ALERTING://对方已震铃
//          NfHfpClientCall.CALL_STATE_INCOMING://来电
//          NfHfpClientCall.CALL_STATE_WAITING://等待...
//          NfHfpClientCall.CALL_STATE_HELD_BY_RESPONSE_AND_HOLD://等待回应
//          NfHfpClientCall.CALL_STATE_TERMINATED://挂断
//         */
//        if(null != clientCall){
//            boolean visible = true;
////            switch (clientCall.getState()){
////                case NfHfpClientCall.CALL_STATE_DIALING://去电
////                {
////                    mPhoneTimeTv.setVisibility(VISIBLE);
////                    mAnswerBt.setVisibility(GONE);
////                }break;
////                case NfHfpClientCall.CALL_STATE_INCOMING://来电
////                {
////                    mPhoneTimeTv.setVisibility(INVISIBLE);
////                    mAnswerBt.setVisibility(VISIBLE);
////                }break;
////                case NfHfpClientCall.CALL_STATE_ACTIVE:{//通话中
////                    mPhoneTimeTv.setVisibility(VISIBLE);
////                    mAnswerBt.setVisibility(GONE);
////                }break;
////                case NfHfpClientCall.CALL_STATE_TERMINATED:{//挂断
////                    visible = false;
////                }break;
////            }
//
//            if(TextUtils.isEmpty(callName)){
//                callName = "陌生人";
//            }
//            mCallNameTv.setText(callName);
//            mPhoneNumberTv.setText("");
//            if(null != mPhoneCallChangedListener){
//                mPhoneCallChangedListener.onPhoneCallChanged(visible);
//            }
//        }
//    }

    @Override
    public void onHfpCallingTimeChanged(String time) {
        if(!TextUtils.isEmpty(time)){
            mPhoneTimeTv.setText(time);
        }
    }

    public void setOnBtPhoneCallChangedListener(OnBtPhoneCallChangedListener listener){
        if(null != listener){
            mPhoneCallChangedListener = listener;
        }
    }

    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.answer_phone_bt:{
                //answer the phone
                onAnswerBtClicked();
            }break;
            case R.id.terminated_phone_bt:{
                //hang up the phone
                onTerminatedBtClicked();
            }break;
            default:
                break;
        }
    }
    */

    private void onAnswerBtClicked(){
        if(null != mNforBtController){
            mNforBtController.answerThePhone();
            mPhoneTimeTv.setVisibility(VISIBLE);
            mAnswerBt.setVisibility(GONE);
        }
    }

    private void onTerminatedBtClicked(){
        if(null != mNforBtController){
            mNforBtController.hangUpThePhone();
        }
    }

    public interface OnBtPhoneCallChangedListener{
        void onPhoneCallChanged(boolean visible);
    }
}
