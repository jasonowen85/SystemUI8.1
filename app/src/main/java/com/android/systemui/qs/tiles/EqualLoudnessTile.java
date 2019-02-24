package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.statusbar.policy.EqualLoudnessController;

/**
 * 等响度Tile
 * @author jingtingy
 * @created 2018/7/18
 */
public class EqualLoudnessTile extends QSTileImpl<BooleanState> implements EqualLoudnessController.EqualLoudnessCallback{
    private final String TAG = "EqualLoudnessTile";

    private final Icon mIcon = ResourceIcon.get(R.drawable.home_pulldown_btn_dengxiangdu_on);

    private EqualLoudnessController mEqualLoudnessController;

    public EqualLoudnessTile(QSHost host) {
        super(host);
        mEqualLoudnessController = Dependency.get(EqualLoudnessController.class);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        if(null != mEqualLoudnessController){
            mEqualLoudnessController.onLoudnessBtClicked();
        }
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        Log.d(TAG,"handleUpdateState");
        if (state.slash == null) {
            state.slash = new SlashState();
        }
        state.label = mHost.getContext().getString(R.string.quick_settings_equal_loudness);
        state.icon = mIcon;
        state.slash.isSlashed = false;

        if(null != mEqualLoudnessController){
            if(mEqualLoudnessController.isLoudnessModeOpen()){
				//等响度打开
                state.state = Tile.STATE_ACTIVE;
            }else {
				//等响度关闭
                state.state = Tile.STATE_INACTIVE;
            }
        }else {
			//等响度按钮不可用
            state.state = Tile.STATE_UNAVAILABLE;
        }
    }

    @Override
    public int getMetricsCategory() {
        return 1146;//MetricsEvent.QS_EQUALLOUDNESS;
    }

    @Override
    public Intent getLongClickIntent() {
        return null;
    }

    @Override
    protected void handleSetListening(boolean listening) {
        if(null == mEqualLoudnessController){
            return;
        }
        if (listening) {
            mEqualLoudnessController.addCallback(this);
        } else {
            mEqualLoudnessController.removeCallback(this);
        }
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_equal_loudness);
    }

    @Override
    public void onLoudnessModeChanged(boolean isOpen) {
        refreshState();
    }
}
