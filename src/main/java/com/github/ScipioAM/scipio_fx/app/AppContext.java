package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.app.config.RootConfig;
import com.github.ScipioAM.scipio_fx.concurrent.ServicePool;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import javafx.concurrent.Service;
import javafx.stage.Stage;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Alan Scipio
 * @since 2022/6/23
 */
public class AppContext {

    private RootConfig rootConfig;

    private ApplicationConfig appConfig;

    private JFXApplication appInstance;

    private Class<? extends JFXApplication> appClass;

    private Stage mainStage;

    private BaseController mainController;

    private ExecutorService threadPool;

    private ServicePool servicePool = new ServicePool();

    private Boolean initThreadFinished = false;

    //====================================================================================================================================

    /**
     * 退出程序
     *
     * @param shutdownNow 是否立马关闭线程池
     */
    public void exit(boolean shutdownNow) {
        appInstance.exit(shutdownNow);
    }

    /**
     * 退出程序
     */
    public void exit() {
        appInstance.exit();
    }

    /**
     * 执行后台子线程
     */
    public void submitTask(Runnable task) {
        if (task != null) {
            getThreadPool().submit(task);
        }
    }

    public <T> Future<T> submitTask(Callable<T> task) {
        if (task != null) {
            return getThreadPool().submit(task);
        } else {
            throw new IllegalArgumentException("task is null while submit java.util.concurrent.Callable type of task");
        }
    }

    public void startService(Class<?> serviceClass) {
        servicePool.startService(serviceClass);
    }

    public Service<?> getService(Class<?> serviceClass) {
        return servicePool.getService(serviceClass);
    }

    public void addService(Service<?> service) {
        servicePool.addService(service);
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

    public ServicePool getServicePool() {
        return servicePool;
    }

    protected void setServicePool(ServicePool servicePool) {
        this.servicePool = servicePool;
    }

    public RootConfig getRootConfig() {
        return rootConfig;
    }

    public void setRootConfig(RootConfig rootConfig) {
        this.rootConfig = rootConfig;
    }

    @SuppressWarnings("unchecked")
    public <T extends RootConfig> T getTrueRootConfig() {
        return (T) rootConfig;
    }

}
