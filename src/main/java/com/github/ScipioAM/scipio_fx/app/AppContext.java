package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

/**
 * @author Alan Scipio
 * @since 2022/6/23
 */
public class AppContext {

    private ApplicationConfig appConfig;

    private JFXApplication appInstance;

    private Class<? extends JFXApplication> appClass;

    private Stage mainStage;

    private BaseController mainController;

    private ExecutorService threadPool;

    private Boolean initThreadFinished = false;

    //====================================================================================================================================

    public void exit(boolean shutdownNow) {
        appInstance.exit(shutdownNow);
    }

    public void exit() {
        appInstance.exit();
    }

    //====================================================================================================================================

    public ApplicationConfig getAppConfig() {
        return appConfig;
    }

    protected void setAppConfig(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    public JFXApplication getAppInstance() {
        return appInstance;
    }

    protected void setAppInstance(JFXApplication appInstance) {
        this.appInstance = appInstance;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    protected void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public BaseController getMainController() {
        return mainController;
    }

    protected void setMainController(BaseController mainController) {
        this.mainController = mainController;
    }

    public Class<? extends JFXApplication> getAppClass() {
        return appClass;
    }

    protected void setAppClass(Class<? extends JFXApplication> appClass) {
        this.appClass = appClass;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    protected void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public Boolean getInitThreadFinished() {
        return initThreadFinished;
    }

    protected void setInitThreadFinished(Boolean initThreadFinished) {
        this.initThreadFinished = initThreadFinished;
    }
}
