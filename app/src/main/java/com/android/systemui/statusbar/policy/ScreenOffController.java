package com.android.systemui.statusbar.policy;

/**
 * 息屏
 * @author jingtingy
 * @created 2018/7/16
 */
public interface ScreenOffController extends CallbackController<ScreenOffController.ScreenStateChangeCallback>{

    /**
     * 判断systemService是否正常连接
     * @return true:已连接  false:未连接 registered
     */
    boolean isSystemServiceConnected();

    /**
     * 注册回调监听
     */	
    void registSystemServiceCallback();

    /**
     * 取消注册监听
     */	
    void unregistSystemServiceCallback();

    /**
     * 请求关闭屏幕
     */
    void requestDisplayOff();

    /**
     * 获取当前屏幕状态
     * @return 0:关屏按钮不可用(异常:systemService未connected或者系统状态为非正常开机状态) 1:亮屏状态  2：关屏状态
     */
    int getDisplayStatus();

    public interface ScreenStateChangeCallback{
         void onScreenStateChanged(int state);
    }
}
