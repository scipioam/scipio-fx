package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.app.config.ConfigLoadListener;
import com.github.ScipioAM.scipio_fx.app.config.RootConfig;
import com.github.ScipioAM.scipio_fx.app.mfx.DefaultMaterialFXInitializer;
import com.github.ScipioAM.scipio_fx.app.mfx.MaterialFXInitializer;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.FXMLViewLoader;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.application.Platform;
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

    public static AppContext context;

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
    protected ExecutorService threadPool;

    /**
     * 位移值，用于（尤其是无边框情况下）可拖拽
     */
    protected double xOffset;
    protected double yOffset;

    public JFXApplication() {
        if (thisClass == null) {
            thisClass = this.getClass();
        }
        //自定义上下文对象
        AppContext customContext = getContext();
        if (customContext == null) {
            //没有自定义，采用默认的
            customContext = new AppContext();
        }
        context = customContext;
        context.setAppClass(thisClass);
        context.setAppInstance(this);
        //创建线程池
        threadPool = buildThreadPool();
        context.setThreadPool(threadPool);
        //准备config对象
        config = buildNewConfigInstance(thisClass);
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
            RootConfig rootConfig = config.loadConfig();
            context.setRootConfig(rootConfig);
            context.setAppConfig(config);
        } catch (Exception e) {
            LaunchListener launchListener = config.getLaunchListenerObj();
            if (launchListener != null) {
                launchListener.onLaunchError(this, e);
            }
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

    //=========================================== ↓↓↓↓↓↓ 程序启动 ↓↓↓↓↓↓ ===========================================

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.mainStage = primaryStage;
            context.setMainStage(primaryStage);
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
                splashScreen.setSplashImgUrl(config.getSplashImgUrl(this.getClass()));
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
            AppInitThread initThread = config.getInitThreadObj();
            if (initThread != null) {
                initThread.setApplication(this).setLaunchListener(config.getLaunchListenerObj());
                threadPool.submit(initThread);
            }
        } catch (Exception e) {
            LaunchListener launchListener = config.getLaunchListenerObj();
            if (launchListener != null) {
                launchListener.onLaunchError(this, e);
            }
            throw e;
        }
    }//end of start()

    @Override
    public void stop() throws Exception {
        if (mainView.getController() != null) {
            mainView.getController().onStop();
        }
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdownNow();
        }
    }

    //=========================================== ↓↓↓↓↓↓ 接口默认实现 ↓↓↓↓↓↓ ===========================================

    public AppContext getContext() {
        return null;
    }

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
        if (mainView != null) {
            //避免没有splash但有AppInitThread时被调用两次
            return mainView;
        }
        //加载
        URL mainViewUrl = config.getMainViewUrl();
        FXMLView mainView = FXMLViewLoader.build().load(mainViewUrl, (Object[]) null);
        BaseController mainController = mainView.getController();
        context.setMainController(mainController);
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
        if (!config.isMainViewResizable()) {
            mainStage.setResizable(false);
        }
        return mainView;
    }

    //=========================================== ↓↓↓↓↓↓ 工具方法 ↓↓↓↓↓↓ ===========================================

    /**
     * 初始化主界面
     */
    protected void initPrimaryStage(Stage primaryStage) {
        //设置图标
        URL iconUrl = config.getIconUrl(this.getClass());
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
        //MaterialFX初始化
        if (config.isUseMaterialFx()) {
            initMaterialFX();
        }

        Scene mainScene;
        LaunchListener launchListener = config.getLaunchListenerObj();
        if (mainStage.isShowing()) {
            //不显示splash，当即调用了一遍showMainView()，但是InitThread又调用了一遍showMainView()
            mainScene = mainStage.getScene();
            if (launchListener != null && config.getInitThreadObj() != null) {
                launchListener.onFinishInit(this, mainView, mainScene);
            }
            return;
        } else {
            //第一次调用showMainView()
            mainScene = new Scene(mainView.getView());
        }

        if (launchListener != null) {
            launchListener.beforeLoadMainScene(mainStage, mainScene);
        }

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
        //主窗口关闭时，必然调用exit方法
        mainStage.setOnCloseRequest(windowsEvent -> exit(false));
        //完成初始化后(显示主界面之前)的回调
        if (launchListener != null && config.getInitThreadObj() == null) {
            launchListener.onFinishInit(this, mainView, mainScene);
        }
        mainStage.show();
        //显示主界面后的回调
        if (launchListener != null) {
            launchListener.afterShowMainView(this, mainView);
        }
    }

    /**
     * MaterialFX初始化
     */
    protected void initMaterialFX() {
        MaterialFXInitializer materialFXInitializer = config.getMaterialFxInitializerObj();
        UserAgentBuilder builder = UserAgentBuilder.builder();
        if (materialFXInitializer == null) {
            materialFXInitializer = new DefaultMaterialFXInitializer();
        }
        materialFXInitializer.init(builder, config.isUseMaterialFxThemeOnly());
    }

    public void setMainView(FXMLView mainView) {
        this.mainView = mainView;
    }

    public void exit(boolean shutdownNow) {
        if (shutdownNow) {
            threadPool.shutdownNow();
        } else {
            threadPool.shutdown();
        }
        Platform.exit();
    }

    public void exit() {
        exit(true);
    }

    /**
     * 创建线程池
     */
    protected ExecutorService buildThreadPool() {
        return Executors.newCachedThreadPool();
    }

}
