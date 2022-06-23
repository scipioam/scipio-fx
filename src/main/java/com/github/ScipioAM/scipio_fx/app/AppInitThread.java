package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.view.FXMLView;
import javafx.application.Platform;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@Setter
@Accessors(chain = true)
public abstract class AppInitThread implements Runnable {

    protected JFXApplication application;
    protected LaunchListener launchListener;

    @Override
    public void run() {
        try {
            //初始化
            init(application);

            //加载mainView
            FXMLView mainView = application.buildMainView();
            application.setMainView(mainView);

            //显示主界面
            Platform.runLater(() -> application.showMainView()); //显示前后皆有回调

        } catch (Exception e) {
            if (launchListener != null) {
                launchListener.onLaunchError(application, e);
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化的具体实现
     *
     * @param application 程序对象
     */
    public abstract void init(JFXApplication application);

}
