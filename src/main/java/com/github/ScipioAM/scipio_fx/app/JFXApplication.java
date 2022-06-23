package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.app.config.ConfigLoadListener;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.FXMLViewLoader;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.net.URL;
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
public abstract class JFXApplication extends Application implements ApplicationInterface {

    protected static Class<? extends JFXApplication> thisClass;

    public final static AppContext context = new AppContext();

    /**
     * app配置
     */
    protected ApplicationConfig config;

    /**
     * 主界面的stage
     */
    protected Stage mainStage;

    /**
     * 主界面view对象
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

    /**
     * 位移值，用于（尤其是无边框情况下）可拖拽
     */
    protected double xOffset;
    protected double yOffset;

    public JFXApplication() {
        if (thisClass == null) {
            thisClass = this.getClass();
        }
        JFXApplication.context.setAppInstance(this);
        //准备config对象
        config = ApplicationConfig.build(thisClass);
        if (this.getClass() == thisClass) {
            config.setAppInstance(this);
        }
        //配置加载时的监听器
        ConfigLoadListener configLoadListener = configLoadListener();
        if (configLoadListener != null) {
            config.setLoadListener(configLoadListener);
        }
        //加载配置
        try {
            config.loadConfig();
        } catch (Exception e) {
            if (config.getLaunchListener() != null) {
                config.getLaunchListener().onLaunchError(this, e);
            }
        }
        JFXApplication.context.setAppConfig(config);
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

    //=========================================== ↓↓↓↓↓↓ 程序启动 ↓↓↓↓↓↓ ===========================================

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.mainStage = primaryStage;
            JFXApplication.context.setMainStage(primaryStage);
            //初始化主界面
            initPrimaryStage(primaryStage);
            //画面显示
            if (splashScreen != null && splashScreen.isVisible()) {
                //显示启动画面
                Stage splashStage = new Stage(StageStyle.TRANSPARENT);
                //设置启动画面的图标
                URL iconUrl = config.getIconUrl();
                if (iconUrl != null) {
                    splashStage.getIcons().add(new Image(iconUrl.toExternalForm()));
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
                showMainView();
            }
            //启动初始化的子线程
            AppInitThread initThread = config.getInitThread();
            if (initThread != null) {
                initThread.setApplication(this).setLaunchListener(config.getLaunchListener());
                threadPool.submit(initThread);
            }
        } catch (Exception e) {
            if (config.getLaunchListener() != null) {
                config.getLaunchListener().onLaunchError(this, e);
            }
            throw e;
        }
    }//end of start()

    @Override
    public void stop() throws Exception {
        if (mainView.getController() != null) {
            mainView.getController().onStop();
        }
    }

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
        //加载
        URL mainViewUrl = config.getMainViewUrl();
        FXMLView mainView = FXMLViewLoader.build().load(mainViewUrl, null);
        BaseController mainController = mainView.getController();
        JFXApplication.context.setMainController(mainController);
        //让主窗体可以被随意拖拽
        if (config.isMainViewDraggable()) {
            Parent rootNode = mainView.getView();
            rootNode.setOnMousePressed(event -> {
                xOffset = mainStage.getX() - event.getScreenX();
                yOffset = mainStage.getY() - event.getScreenY();
            });
            rootNode.setOnMouseDragged(event -> {
                mainStage.setX(event.getScreenX() + xOffset);
                mainStage.setY(event.getScreenY() + yOffset);
            });
        }
        return mainView;
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
        //StageStyle
        StageStyle stageStyle = config.getMainStageStyle();
        if (stageStyle != null) {
            primaryStage.initStyle(stageStyle);
        }
    }

    /**
     * 显示主界面
     */
    protected void showMainView() {
        Scene mainScene = new Scene(mainView.getView());
        mainStage.setScene(mainScene);
        if (splashScreen != null && splashScreen.isVisible()) {
            Stage splashStage = splashScreen.getStage();
            splashStage.close();
            splashStage.setScene(null);
            splashScreen.setStage(null);
        }
        BaseController mainController = mainView.getController();
        mainController.setParentStage(mainStage);
        mainView.setStage(mainStage);
        //完成初始化后(显示主界面之前)的回调
        if (config.getLaunchListener() != null) {
            config.getLaunchListener().onFinishInit(this, mainView, mainScene);
        }
        mainStage.show();
        //显示主界面后的回调
        if (config.getLaunchListener() != null) {
            config.getLaunchListener().afterShowMainView(this, mainView);
        }
    }

    public void setMainView(FXMLView mainView) {
        this.mainView = mainView;
    }
}
