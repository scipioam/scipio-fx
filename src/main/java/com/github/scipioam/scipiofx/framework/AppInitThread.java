package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import com.github.scipioam.scipiofx.view.SplashScreen;
import javafx.application.Platform;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@SuppressWarnings({"UnusedReturnValue", "LombokSetterMayBeUsed", "LombokGetterMayBeUsed"})
public abstract class AppInitThread implements Runnable {

    /**
     * 默认的启动画面显示时间（毫秒）
     */
    protected long defaultSplashTime = 2000L;

    /**
     * 启动画面的进度是否无限循环
     */
    protected boolean endlessSplashProgress = true;

    protected SplashScreen splashScreen;

    protected JFXApplication application;

    protected AppContext context;

    protected LaunchListener launchListener;

    protected boolean needRunLater = true;

    protected boolean finished = false;

    @Override
    public void run() {
        needRunLater = true;
        finished = false;
        try {
            long startTime = System.currentTimeMillis();

            beforeInit(application, context);

            // 自定义初始化操作
            init(application, context);

//            afterInit(application, context);

            long costTime = System.currentTimeMillis() - startTime;
            if (application.getSplashScreen() != null && costTime < defaultSplashTime) {
                // 如果初始化时间过快，则延迟一段时间再显示主界面
                Thread.sleep(defaultSplashTime - costTime);
            }
        } catch (Exception e) {
            if (launchListener != null) {
                launchListener.onLaunchError(application, e);
            } else {
                e.printStackTrace();
                Platform.runLater(() -> {
                    needRunLater = false;
                    CFXDialogHelper.showExceptionDialog(e);
                });
            }
        } finally {
            needRunLater = false;
            finished = true;
        }
    }

    /**
     * 初始化的具体实现（非UI线程）
     *
     * @param application 程序对象
     * @param context     上下文对象
     */
    public abstract void init(JFXApplication application, AppContext context) throws Exception;

    /**
     * 初始化之前的回调（非UI线程）
     */
    protected void beforeInit(JFXApplication application, AppContext context) throws Exception {
        // do nothing
    }

    /**
     * 初始化之后的回调（UI线程）
     */
    protected void afterInit(JFXApplication application, AppContext context) throws Exception {
        // do nothing
    }

    /**
     * 初始化结束时（显示主画面之前）的回调（UI线程）
     */
    public void onFinished(FXMLView mainView) {
        if (mainView != null) {
            BaseController mainController = mainView.getController();
            mainController.onInitThreadFinished();
        }
    }

    public void setApplication(JFXApplication application) {
        this.application = application;
    }

    public void setLaunchListener(LaunchListener launchListener) {
        this.launchListener = launchListener;
    }

    public void setContext(AppContext context) {
        this.context = context;
    }

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    /**
     * 设置启动画面的进度
     *
     * @param progress 进度（0.0~1.0）
     */
    public void setSplashProgress(double progress) {
        if (!endlessSplashProgress && splashScreen != null) {
            if (needRunLater) {
                Platform.runLater(() -> splashScreen.setProgress(progress));
            } else {
                splashScreen.setProgress(progress);
            }
        }
    }

    public boolean isEndlessSplashProgress() {
        return endlessSplashProgress;
    }

    public void setEndlessSplashProgress(boolean endlessSplashProgress) {
        this.endlessSplashProgress = endlessSplashProgress;
    }

    public long getDefaultSplashTime() {
        return defaultSplashTime;
    }

    public void setDefaultSplashTime(long defaultSplashTime) {
        if (defaultSplashTime < 0L) {
            throw new IllegalArgumentException("defaultSplashTime must be greater than or equal to 0");
        }
        this.defaultSplashTime = defaultSplashTime;
    }

    public boolean isFinished() {
        return finished;
    }
}
