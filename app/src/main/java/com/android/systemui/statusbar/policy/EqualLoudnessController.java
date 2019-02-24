package com.android.systemui.statusbar.policy;

/**
 * @author jingtingy
 * @desc 等响度开关控制
 * @created 2018/8/2
 */
public interface EqualLoudnessController extends CallbackController<EqualLoudnessController.EqualLoudnessCallback>{
    /**
     * 判断等响度是否打开
     * @return true:打开 false:未打开
     */
    boolean isLoudnessModeOpen();

    /**
     * 设置打开等响度
     */
    void openLoudnessMode();

    /**
     * 设置关闭等响度
     */
    void closeLoudnessMode();

    /**
     * 点击等响度按钮
     */
    void onLoudnessBtClicked();

    public interface EqualLoudnessCallback{
        /**
         * 等响度开关回调监听
         * @param isOpen true:已打开 false:已关闭
         */
        void onLoudnessModeChanged(boolean isOpen);
    }
}
