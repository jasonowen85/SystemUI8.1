package com.android.systemui.statusbar.policy;

/**
 * @author jingtingy
 * @desc 系统静音
 * @created 2018/8/2
 */
public interface MuteController extends CallbackController<MuteController.MuteStateChangeCallback>{

    /**
     * 判断系统是否静音
     * @return true:已静音 false:未静音
     */
    boolean isSystemMuted();

    /**
     * 设置系统静音
     */
    void setSystemMute();

    /**
     * 取消系统静音
     */
    void cancelSystemMute();

    public interface MuteStateChangeCallback{
        /**
         * 系统静音状态回调监听
         * @param isMuted true:静音打开 false:静音关闭
         */
         void onMuteStateChanged(boolean isMuted);
    }
}
