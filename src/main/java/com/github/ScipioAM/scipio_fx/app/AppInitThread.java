package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.view.FXMLView;
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

            FXMLView mainView = application.buildMainView();

            //初始化完成的回调
            if (launchListener != null) {
                launchListener.onFinishInit(application, mainView);
            }

            //显示主界面
            application.showMainView();

            //显示主界面后的回调
            if (launchListener != null) {
                launchListener.afterShowMainView(application, mainView);
            }
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
