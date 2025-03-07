package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import com.github.scipioam.scipiofx.view.SplashScreen;
import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
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

            //自定义初始化操作
            init(application, context);

            long costTime = System.currentTimeMillis() - startTime;
            if (splashScreen != null && costTime < defaultSplashTime) {
                //如果初始化时间过快，则延迟一段时间再显示主界面
                Thread.sleep(defaultSplashTime - costTime);
            }

            //UI主线程回调
            Platform.runLater(() -> {
                needRunLater = false;
                try {
                    if (splashScreen != null) {
                        // 加载mainView之前的回调
                        beforeShowMainView();
                        //加载mainView
                        application.initMainStage();
                        //显示mainView
                        application.showMainView();
                    }
                    //初始化结束时的回调
                    FXMLView mainView = application.getMainView();
                    //初始化结束时的回调
                    onFinished(mainView);
                } catch (Exception e) {
                    e.printStackTrace();
                    CFXDialogHelper.showExceptionDialog(e);
                }
            });
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
            finished = true;
        }
    }

    /**
     * 初始化的具体实现（在{@link Platform#runLater(Runnable)}之外）
     *
     * @param application 程序对象
     * @param context     上下文对象
     */
    public abstract void init(JFXApplication application, AppContext context) throws Exception;

    /**
     * 加载mainView之前的回调（在{@link Platform#runLater(Runnable)}之内）
     */
    protected void beforeShowMainView() {
        // do nothing
    }

    /**
     * 初始化结束时（显示主画面之前）的回调（在{@link Platform#runLater(Runnable)}之内）
     */
    protected void onFinished(FXMLView mainView) {
        BaseController mainController = mainView.getController();
        mainController.onInitThreadFinished();
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
