package apptest;

import com.github.ScipioAM.scipio_fx.app.JFXApplication;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.app.config.RootConfig;
import com.github.ScipioAM.scipio_fx.view.FXMLView;

public class TestApplication extends JFXApplication implements LaunchListener {

    private static long startTime;

    public static void main(String[] args) {
        try {
            startTime = System.currentTimeMillis();
            launchApp(TestApplication.class, args, false);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public String configPrefix() {
        return "[example]fx-app";
    }

    @Override
    public LaunchListener bindLaunchListener() {
        return this;
    }

    @Override
    public Class<? extends RootConfig> getRootConfigType() {
        return TestRootConfig.class;
    }

    //==============================================================================================================================

    @Override
    public void afterShowMainView(JFXApplication app, FXMLView mainView) {
        System.out.println("启动耗时(ms): " + (System.currentTimeMillis() - startTime));
    }

}