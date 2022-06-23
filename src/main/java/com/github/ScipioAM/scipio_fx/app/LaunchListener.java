package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.view.FXMLView;
import javafx.scene.Scene;

/**
 * 启动监听器
 *
 * @author Alan Scipio
 * @since 2022/2/22
 */
public interface LaunchListener {

    /**
     * 完成初始化后(显示主界面之前)
     *
     * @param mainView 主界面对象
     */
    default void onFinishInit(JFXApplication app, FXMLView mainView, Scene mainScene) {
    }

    /**
     * 显示主界面后的回调
     *
     * @param mainView 主界面对象
     */
    default void afterShowMainView(JFXApplication app, FXMLView mainView) {
    }

    /**
     * 启动出现异常时
     *
     * @param app 程序对象
     * @param e   异常
     */
    default void onLaunchError(JFXApplication app, Exception e) {
        e.printStackTrace();
    }

}
