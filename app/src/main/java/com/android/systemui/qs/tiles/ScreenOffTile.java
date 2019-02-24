package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.qs.QSTile.BooleanState;
import com.android.systemui.statusbar.policy.ScreenOffController;
import com.android.systemui.statusbar.policy.ScreenOffControllerImpl;

/**
 * 自定义锁屏Tile
 * @author jingtingy
 * @created 2018/7/16
 */
public class ScreenOffTile extends QSTileImpl<BooleanState> implements ScreenOffController.ScreenStateChangeCallback{
    public static final String TAG = "ScreenOffTile";
    public static final boolean MY_DEBUG = false;
    private final Icon mIcon = ResourceIcon.get(R.mipmap.home_pulldown_btn_close);

    private ScreenOffController mScreenOffController;

    public ScreenOffTile(QSHost host) {
        super(host);

        mScreenOffController = Dependency.get(ScreenOffController.class);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        if(MY_DEBUG)Log.d(TAG,"handleClick");
        if(null != mScreenOffController){
            mScreenOffController.requestDisplayOff();
        }else {
            if(MY_DEBUG)Log.d(TAG,"mScreenOffController == null");
        }
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        if(MY_DEBUG)Log.d(TAG,"handleUpdateState");
        if (state.slash == null) {
            state.slash = new SlashState();
        }
        state.label = mHost.getContext().getString(R.string.quick_settings_screen_off);
        state.icon = mIcon;
        state.slash.isSlashed = false;
        if(null != mScreenOffController){
            switch (mScreenOffController.getDisplayStatus()){
                case ScreenOffControllerImpl.SCREEN_STATE_UNAVAILABLE:{
                    state.state = Tile.STATE_UNAVAILABLE;
                }break;
                case ScreenOffControllerImpl.SCREEN_DISPLAY_ON:{
                    state.state = Tile.STATE_INACTIVE;
                }break;
                case ScreenOffControllerImpl.SCREEN_DISPLAY_OFF:{
                    state.state = Tile.STATE_ACTIVE;
                }break;
            }
        }else {
            if(MY_DEBUG)Log.d(TAG,"mScreenOffController == null");
        }
    }

    @Override
    public int getMetricsCategory() {
        return 1144;//MetricsEvent.QS_SCREENOFF;
    }

    @Override
    public Intent getLongClickIntent() {
        return new Intent("android.settings.NIGHT_DISPLAY_SETTINGS");
    }

    @Override
    protected void handleSetListening(boolean listening) {
        if(null == mScreenOffController)return;
        if(listening){
            mScreenOffController.addCallback(this);
        }else {
            mScreenOffController.removeCallback(this);
        }
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_screen_off);
    }

    @Override
    public void onScreenStateChanged(int state) {
        if(MY_DEBUG)Log.d(TAG,"onScreenStateChanged");
        refreshState();
    }
}
