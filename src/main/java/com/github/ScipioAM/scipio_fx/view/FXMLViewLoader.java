package com.github.ScipioAM.scipio_fx.view;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * fxml加载器
 *
 * @author Alan Scipio
 * @since 2022/2/22
 */
public class FXMLViewLoader {

    private final FXMLLoader fxmlLoader;

    public static FXMLViewLoader build() {
        return new FXMLViewLoader();
    }

    private FXMLViewLoader() {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
    }

    /**
     * 加载fxml文件
     *
     * @param location fxml文件的路径
     * @param initArg  初始化参数
     * @return fxml对象
     * @throws IOException 找不到文件或加载失败
     */
    public FXMLView load(URL location, Object initArg) throws IOException {
        fxmlLoader.setLocation(location);
        //加载fxml文件 (controller的onCreate方法被回调)
        Parent rootNode = fxmlLoader.load();
        //获取controller对象
        BaseController controller = fxmlLoader.getController();
        //controller初始化回调
        controller.onLoadInit(rootNode, initArg);
        return new FXMLView(rootNode, controller, location.getPath());
    }

    public FXMLView load(String fxmlPath, Object initArg) throws IOException {
        URL location = getClass().getResource(fxmlPath);
        if (location == null) {
            location = new URL(fxmlPath);
        }
        return load(location, initArg);
    }

    public FXMLView load(String fxmlPath) throws IOException {
        return load(fxmlPath, null);
    }

}
