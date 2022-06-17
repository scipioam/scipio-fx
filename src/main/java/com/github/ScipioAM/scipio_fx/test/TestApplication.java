package com.github.ScipioAM.scipio_fx.test;

import com.github.ScipioAM.scipio_fx.app.JFXApplication;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.view.FXMLView;

import java.net.URL;

public class TestApplication extends JFXApplication implements LaunchListener {

    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        launchApp(TestApplication.class, args, false);
    }

    @Override
    public URL mainViewUrl() {
        return getClass().getResource("/views/main.fxml");
    }

    @Override
    public LaunchListener bindLaunchListener() {
        return this;
    }

    //==============================================================================================================================

    @Override
    public void afterShowMainView(JFXApplication app, FXMLView mainView) {
        System.out.println("启动耗时(ms): " + (System.currentTimeMillis() - startTime));
    }

}