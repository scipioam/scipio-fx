package apptest;

import com.github.ScipioAM.scipio_fx.app.JFXApplication;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.app.config.ConfigLoadListener;
import com.github.ScipioAM.scipio_fx.app.config.RootConfig;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import org.yaml.snakeyaml.Yaml;

public class TestApplication extends JFXApplication implements LaunchListener, ConfigLoadListener {

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
    public ConfigLoadListener configLoadListener() {
        return this;
    }

    @Override
    public LaunchListener bindLaunchListener() {
        return this;
    }

    @Override
    public ApplicationConfig buildNewConfigInstance(Class<? extends JFXApplication> thisClass) {
        return ApplicationConfig.build(TestRootConfig.class, thisClass);
    }

    //==============================================================================================================================


    @Override
    public void afterLoad(Yaml yaml, RootConfig rootConfig, ApplicationConfig config) {
        System.out.println("read config from: " + config.configFileName());
        TestRootConfig testRootConfig = (TestRootConfig) rootConfig;
        System.out.println(testRootConfig.getTestA());
    }

    @Override
    public void afterShowMainView(JFXApplication app, FXMLView mainView) {
        System.out.println("启动耗时(ms): " + (System.currentTimeMillis() - startTime));
    }

}