package com.android.systemui.qs;

import java.lang.Integer;
import android.os.Parcel;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.settings.ToggleSeekBar;
import com.android.systemui.statusbar.policy.BrightnessController;


/**
 * @author jingtingy
 * @desc 音量调节控件
 * @created 2018/8/6
 */
public class BrightnessSliderView extends RelativeLayout  {
    private final String TAG = "BrightnessSliderView";

    private Context mContext;
    private ImageView mbrightnessIconIv;
    private ToggleSeekBar mbrightnessSlider;
    private TextView mbrightnessValueTv;
    private BrightnessController mBrightnessController;


    protected boolean mListening;
//    private VolumeController mVolumeController;

    public BrightnessSliderView(Context context) {
        this(context,null);
    }

    public BrightnessSliderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BrightnessSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;


       
        LayoutInflater.from(context).inflate(R.layout.qs_brightness_slider_layout, this, true);
        mbrightnessIconIv = findViewById(R.id.brightness_icon);
        mbrightnessSlider = findViewById(R.id.brightness_slider);
        mbrightnessValueTv = findViewById(R.id.tv_brightness_text);
        mbrightnessSlider.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mBrightnessController = Dependency.get(BrightnessController.class);
    }

    /**
     * 设置调节监听
     * @param listening 监听状态 true:监听中 false:未监听
     */
    public void setListening(boolean listening) {
////        if(null == mVolumeController)return;
//        if (mListening == listening) return;
//        mListening = listening;
//        if (mVolumeSlider.getVisibility() == View.VISIBLE) {
//            if (listening) {
//                mVolumeController.addCallback(this);
//            } else {
//                mVolumeController.removeCallback(this);
//            }
//        }
    }

    /**
     * 刷新亮度控件
     * @param  当前亮度
     */
    protected void refreshState(int volume){
        if(null != mbrightnessValueTv){
            mbrightnessValueTv.setText(String.valueOf(volume));
        }

        if(null != mbrightnessIconIv){

        }

        if(null != mbrightnessSlider){
            mbrightnessSlider.setProgress(volume);
        }
    }




    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        boolean isFromUser = false;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            isFromUser = fromUser;
            if(null != mbrightnessValueTv){
                if(mBrightnessController.getMax()>=seekBar.getProgress() && mBrightnessController.getMin()<=seekBar.getProgress()) {
                    mbrightnessValueTv.setText(String.valueOf(progress));
                }
            }
            if(null != mBrightnessController && isFromUser){
                if(null != mBrightnessController && isFromUser){
                    if(mBrightnessController.getMax()>=seekBar.getProgress() && mBrightnessController.getMin()<=seekBar.getProgress()){
                        mBrightnessController.setProgress(seekBar.getProgress());
                    }
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(null != mBrightnessController && isFromUser){
                if(null != mBrightnessController && isFromUser){
                    if(mBrightnessController.getMax()>=seekBar.getProgress() && mBrightnessController.getMin()<=seekBar.getProgress()){
                    mBrightnessController.setProgress(seekBar.getProgress());
                    }
                }
            }
        }
    };
}
