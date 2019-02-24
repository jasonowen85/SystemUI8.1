package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.statusbar.policy.VolumeController;

/**
 * 静音Tile
 * @author jingtingy
 * @created 2018/7/18
 */
public class MuteTile extends QSTileImpl<BooleanState> implements VolumeController.VolumeChangeCallback{
    private final String TAG = "MuteTile";

    private final Icon mIcon = ResourceIcon.get(R.drawable.home_pulldown_btn_mute);
    private VolumeController mVolumeController;

    public MuteTile(QSHost host) {
        super(host);
        mVolumeController = Dependency.get(VolumeController.class);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        if(null != mVolumeController){
            mVolumeController.setSystemMute();
        }else {
            Log.d(TAG,"mVolumeController == null");
        }
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        if (state.slash == null) {
            state.slash = new SlashState();
        }
        state.label = mHost.getContext().getString(R.string.quick_settings_mute);
        state.slash.isSlashed = false;
        state.icon = mIcon;
        state.state = Tile.STATE_UNAVAILABLE;
        if(null != mVolumeController){
            if(mVolumeController.isSystemMuted()){
				//静音打开
                state.state = Tile.STATE_ACTIVE;
            }else {
				//静音关闭
                state.state = Tile.STATE_INACTIVE;
            }
        }else {
			//静音按钮不可用
            state.state = Tile.STATE_UNAVAILABLE;
        }
    }

    @Override
    public int getMetricsCategory() {
        return 1145;//MetricsEvent.QS_MUTE;
    }

    @Override
    public Intent getLongClickIntent() {
        return null;
    }

    @Override
    protected void handleSetListening(boolean listening) {
        if(null == mVolumeController){
            return;
        }
        if (listening) {
            mVolumeController.addCallback(this);
        } else {
            mVolumeController.removeCallback(this);
        }
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_mute);
    }

    @Override
    public void onVolumeStateChanged(boolean isMuted, int volumeValue) {
        refreshState();
    }
}
