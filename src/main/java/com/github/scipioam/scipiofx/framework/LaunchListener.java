package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    default void beforeShowMainView(JFXApplication app, FXMLView mainView, Scene mainScene) {
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
    default void onLaunchError(JFXApplication app, Throwable e) {
        e.printStackTrace();
        Platform.runLater(() -> CFXDialogHelper.showExceptionDialog(e));
    }

    /**
     * 加载主画面Scene前
     *
     * @param mainStage 主stage对象
     * @param mainScene 主scene对象
     */
    default void beforeLoadMainScene(Stage mainStage, Scene mainScene) {
    }

}
