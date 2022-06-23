package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import javafx.stage.Stage;

/**
 * @author Alan Scipio
 * @since 2022/6/23
 */
public class AppContext {

    private ApplicationConfig appConfig;

    private JFXApplication appInstance;

    private Stage mainStage;

    private BaseController mainController;

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

}
