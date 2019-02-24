package com.android.systemui.statusbar.policy;

/**
 * @author jingtingy
 * @desc 音量调节控制
 * @created 2018/8/12
 */
public interface VolumeController extends CallbackController<VolumeController.VolumeChangeCallback>{
    /**
     * 判断系统是否静音
     * @return true:已静音  false:未静音
     */
    boolean isSystemMuted();

    /**
     * 设置系统静音
     */
    void setSystemMute();

    /**
     * 设置音量进度
     * @param progress 音量大小(范围0-40)
     */
    void setProgress(int progress);

    /**
     * 获取最大音量
     * @return 最大音量
     */
    int getMax();

    /**
     * 获取当前系统音量
     * @return 当前系统音量
     */
    int getProgress();

    public interface VolumeChangeCallback{
        /**
         * 音量改变状态监听
         * @param isMuted true:系统静音 false:系统未静音
         * @param volumeValue 当前系统音量
         */
        void onVolumeStateChanged(boolean isMuted,int volumeValue);
    }
}
