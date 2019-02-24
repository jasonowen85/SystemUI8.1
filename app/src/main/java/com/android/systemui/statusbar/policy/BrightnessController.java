package com.android.systemui.statusbar.policy;


/**
 * @author jingtingy
 * @desc 亮度调节控制
 * @created 2018/8/12
 */
public interface BrightnessController {


    /**
     * 设置亮度进度
     * @param progress 亮度大小(范围1-9)
     */
    void setProgress(int progress);

    /**
     * 获取最大亮度
     * @return 最大亮度
     */
    int getMax();

    /**
     * 获取最小亮度
     * @return 最小亮度
     */
    int getMin();

    /**
     * 获取当前系统亮度
     * @return 当前系统亮度
     */
    int getProgress();


}
