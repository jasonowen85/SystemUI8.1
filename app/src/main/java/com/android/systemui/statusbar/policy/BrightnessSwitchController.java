package com.android.systemui.statusbar.policy;

/**
 * @author jingtingy
 * @desc 背光控制开关
 * @created 2018/11/23
 */
public interface BrightnessSwitchController extends CallbackController<BrightnessSwitchController.BrightnessChangeCallback>{
    /**
     * 请求关闭屏幕
     */
    void requestDisplayOff();

    /**
     * 获取当前屏幕状态
     * @return 0:关屏按钮不可用(异常:systemService未connected或者系统状态为非正常开机状态) 1:亮屏状态  2：关屏状态
     */
    int getDisplayStatus();

    public interface BrightnessChangeCallback{
        void onBrightnessChanged(boolean isOp);
    }
}
