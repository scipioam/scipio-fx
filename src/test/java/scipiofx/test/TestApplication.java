package scipiofx.test;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.framework.LaunchListener;
import com.github.scipioam.scipiofx.framework.config.ConfigRootProperties;
import com.github.scipioam.scipiofx.framework.fxml.FXMLView;

public class TestApplication extends JFXApplication implements LaunchListener {

    private static long startTime;

    public static void main(String[] args) {
        try {
            startTime = System.currentTimeMillis();
            launch(TestApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public Class<? extends ConfigRootProperties> configRootPropertiesType() {
        return TestRootConfig.class;
    }

    @Override
    public AppContext buildContext() {
        return new TestAppContext();
    }

    @Override
    public String configPrefix() {
        return "[example]app-config";
    }

    @Override
    public LaunchListener launchListener() {
        return this;
    }

    @Override
    public void afterShowMainView(JFXApplication app, FXMLView mainView) {
        long endTime = System.currentTimeMillis();
        System.out.println("Launch cost: " + (endTime - startTime) + "ms");
    }
}