package com.android.systemui.qs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.settings.ToggleSeekBar;
import com.android.systemui.statusbar.policy.VolumeController;

/**
 * @author jingtingy
 * @desc 音量调节控件
 * @created 2018/8/6
 */
public class VolumeSliderView extends RelativeLayout implements VolumeController.VolumeChangeCallback {
    private final String TAG = "VolumeSliderView";
    private ImageView mVolumeIconIv;
    private ToggleSeekBar mVolumeSlider;
    private TextView mVolumeValueTv;

    protected boolean mListening;
    private VolumeController mVolumeController;

    public VolumeSliderView(Context context) {
        this(context,null);
    }

    public VolumeSliderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VolumeSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.qs_volume_slider_layout, this, true);
        mVolumeIconIv = findViewById(R.id.iv_volume_icon);
        mVolumeSlider = findViewById(R.id.sbar_volume_value);
        mVolumeValueTv = findViewById(R.id.tv_volume_text);

        mVolumeSlider.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mVolumeController = Dependency.get(VolumeController.class);
    }

    /**
     * 设置音量调节监听
     * @param listening 监听状态 true:监听中 false:未监听
     */	
    public void setListening(boolean listening) {
        if(null == mVolumeController)return;
        if (mListening == listening) return;
        mListening = listening;
        if (mVolumeSlider.getVisibility() == View.VISIBLE) {
            if (listening) {
                mVolumeController.addCallback(this);
            } else {
                mVolumeController.removeCallback(this);
            }
        }
    }

    /**
     * 刷新音量控件
     * @param volume 当前音量
     */
    protected void refreshState(int volume){
        if(null != mVolumeValueTv){
            mVolumeValueTv.setText(String.valueOf(volume));
        }

        if(null != mVolumeIconIv){
            mVolumeIconIv.setImageResource(volume == 0 ?
                    com.android.systemui.R.drawable.ic_qs_volume_bar_mute :
                    com.android.systemui.R.drawable.ic_qs_volume_bar);
        }

        if(null != mVolumeSlider){
            mVolumeSlider.setProgress(volume);
        }
    }

    @Override
    public void onVolumeStateChanged(boolean isMuted, int volumeValue) {
        refreshState(volumeValue);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        boolean isFromUser = false;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            isFromUser = fromUser;
            if(null != mVolumeValueTv){
                mVolumeValueTv.setText(String.valueOf(progress));
            }
            if(null != mVolumeController && isFromUser){
                mVolumeController.setProgress(seekBar.getProgress());
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(null != mVolumeController && isFromUser){
                mVolumeController.setProgress(seekBar.getProgress());
            }
        }
    };
}
