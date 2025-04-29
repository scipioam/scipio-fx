package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import javafx.application.Platform;
import javafx.scene.Scene;

/**
 * 管理主画面加载和初始化线程的状态，确保在正确的时机进行调用。 <br/>
 * （启动必要条件：初始化子线程不为空）
 *
 * @author Alan Scipio
 * created on 2025-04-29
 */
final class StateCheckThread implements Runnable {

    private final LogHelper log;

    private final JFXApplication application;
    private final AppInitThread initThread;

    public StateCheckThread(JFXApplication application, AppInitThread initThread) {
        this.application = application;
        this.initThread = initThread;
        this.log = new LogHelper(getClass());
    }

    @Override
    public void run() {
        while (true) {
            if (application.getSplashScreen() == null) {
                // 启动画面为空，需要检查主画面是否加载完毕，以及初始化子线程是否完成
                FXMLView mainView = application.getMainView();
                if (mainView != null && initThread.isFinished()) {
                    // 只需要子线程回调，不需要负责构建主画面
                    Platform.runLater(() -> {
                        try {
                            initThread.afterInit(application, application.context);
                            initThread.onFinished(mainView);
                        } catch (Exception e) {
                            log.error("Error occurred while executing onFinished() of initThread.", e);
                        }
                    });
                    break;
                }
            } else {
                // 启动画面不为空，则只检查初始化子线程是否执行完
                if (initThread.isFinished()) {
                    // 需要负责构建主画面
                    Platform.runLater(() -> {
                        try {
                            // 加载mainView的内容
                            Scene mainScene = application.buildMainScene();
                            // 加载mainView之前的回调
                            initThread.afterInit(application, application.context);
                            // 初始化mainView
                            application.initMainStage(mainScene);
                            // 显示mainView
                            application.showMainView();
                            // 初始化结束时的回调
                            FXMLView mainView = application.getMainView();
                            // 初始化结束时的回调
                            initThread.onFinished(mainView);
                        } catch (Exception e) {
                            e.printStackTrace();
                            CFXDialogHelper.showExceptionDialog(e);
                        }
                    });
                    break;
                }
            }
        }
        log.info("StateCheckThread finished his job.");
    }

}
