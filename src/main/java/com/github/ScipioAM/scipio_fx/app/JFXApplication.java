package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.FXMLViewLoader;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应用程序
 *
 * @author Alan Scipio
 * @since 2022/2/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Setter(AccessLevel.NONE)
public class JFXApplication extends Application implements ApplicationInterface {

    protected static Class<? extends JFXApplication> thisClass;

    /**
     * app配置
     */
    protected ApplicationConfig config;

    /**
     * 主界面的stage
     */
    protected Stage primaryStage;

    /**
     * 主界面对线
     */
    protected FXMLView mainView;

    /**
     * 启动界面
     */
    protected static SplashScreen splashScreen = null;

    /**
     * 线程池
     */
    protected final ExecutorService threadPool = Executors.newCachedThreadPool();

    public JFXApplication() {
        if (thisClass == null) {
            thisClass = this.getClass();
        }
        config = ApplicationConfig.build(thisClass);
        if (this.getClass() == thisClass) {
            config.setAppInstance(this);
        }
    }

    //=========================================== ↓↓↓↓↓↓ 启动入口API ↓↓↓↓↓↓ ===========================================

    /**
     * 程序启动
     *
     * @param appClass     app启动类
     * @param args         main方法的入参
     * @param splashScreen 自定义启动画面
     */
    public static void launchApp(final Class<? extends JFXApplication> appClass, String[] args, SplashScreen splashScreen) {
        JFXApplication.splashScreen = splashScreen;
        JFXApplication.thisClass = appClass;
        launch(appClass, args);
    }

    /**
     * 程序启动
     *
     * @param appClass      app启动类
     * @param args          main方法的入参
     * @param splashVisible 是否显示启动画面(构建默认的启动画面)
     */
    public static void launchApp(final Class<? extends JFXApplication> appClass, String[] args, boolean splashVisible) {
        JFXApplication.splashScreen = splashVisible ? new SplashScreen() : null;
        JFXApplication.thisClass = appClass;
        launch(appClass, args);
    }

    /**
     * 程序启动(没有启动画面)
     *
     * @param appClass app启动类
     * @param args     main方法的入参
     */
    public static void launchApp(final Class<? extends JFXApplication> appClass, String[] args) {
        JFXApplication.thisClass = appClass;
        launch(appClass, args);
    }

    public static void launchApp(String[] args, SplashScreen splashScreen) {
        launchApp(JFXApplication.class, args, splashScreen);
    }

    public static void launchApp(String[] args, boolean splashVisible) {
        launchApp(JFXApplication.class, args, splashVisible);
    }

    public static void launchApp(String[] args) {
        launchApp(JFXApplication.class, args, false);
    }

    //=========================================== ↓↓↓↓↓↓ 程序启动 ↓↓↓↓↓↓ ===========================================

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.primaryStage = primaryStage;
            //加载配置
            config.loadConfig();
            //初始化主界面
            initPrimaryStage(primaryStage);
            //画面显示
            if (splashScreen != null && splashScreen.isVisible()) {
                //显示启动画面
                Stage splashStage = new Stage(StageStyle.TRANSPARENT);
                String iconPath = config.getIconPath();
                if (StringUtils.isNotNull(iconPath)) {
                    splashStage.getIcons().addAll(new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toExternalForm()));
                }
                splashScreen.setSplashImgUrl(config.getSplashImgUrl());
                Scene splashScene = new Scene(splashScreen.buildViews(), Color.TRANSPARENT);
                splashStage.setScene(splashScene);
                splashStage.setResizable(false);
                splashScreen.setStage(splashStage);
                splashStage.show();
            } else {
                //不显示启动画面，直接显示主画面
                mainView = buildMainView();
                if (config.getLaunchListener() != null) {
                    config.getLaunchListener().onFinishInit(this, mainView);
                }
                showMainView();
                //显示主界面后的回调
                if (config.getLaunchListener() != null) {
                    config.getLaunchListener().afterShowMainView(this, mainView);
                }
            }
            //启动初始化的子线程
            AppInitThread initThread = bindInitThread();
            if (initThread != null) {
                initThread.setApplication(this).setLaunchListener(config.getLaunchListener());
                threadPool.submit(initThread);
            }
        } catch (Exception e) {
            if (config.getLaunchListener() != null) {
                config.getLaunchListener().onLaunchError(this, e);
            } else {
                throw e;
            }
        }
    }//end of start()

    //=========================================== ↓↓↓↓↓↓ 接口默认实现 ↓↓↓↓↓↓ ===========================================

    @Override
    public String title() {
        return null;
    }

    @Override
    public URL iconUrl() {
        return null;
    }

    @Override
    public URL mainViewUrl() {
        return null;
    }

    @Override
    public URL splashImgUrl() {
        return null;
    }

    @Override
    public FXMLView buildMainView() throws IOException {
        URL mainViewUrl = config.getMainViewUrl();
        return FXMLViewLoader.build().load(mainViewUrl, null);
    }

    //=========================================== ↓↓↓↓↓↓ 工具方法 ↓↓↓↓↓↓ ===========================================

    /**
     * 初始化主界面
     */
    protected void initPrimaryStage(Stage primaryStage) throws IOException {
        //设置图标
        URL iconUrl = config.getIconUrl();
        if (iconUrl != null) {
            primaryStage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }
        //设置标题
        String title = config.getTitle();
        if (StringUtils.isNotNull(title)) {
            primaryStage.setTitle(title);
        }
    }

    /**
     * 显示主界面
     */
    protected void showMainView() {
        primaryStage.setScene(new Scene(mainView.getView()));
        if (splashScreen != null && splashScreen.isVisible()) {
            Stage splashStage = splashScreen.getStage();
            splashStage.close();
            splashStage.setScene(null);
            splashScreen.setStage(null);
        }
        BaseController mainController = mainView.getController();
        mainController.setThisStage(primaryStage);
        primaryStage.show();
    }

}
