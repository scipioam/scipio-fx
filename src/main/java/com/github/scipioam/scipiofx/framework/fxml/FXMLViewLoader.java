package com.github.scipioam.scipiofx.framework.fxml;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@Getter
public class FXMLViewLoader {

    private final FXMLLoader fxmlLoader;

    public FXMLViewLoader() {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
    }

    /**
     * 加载fxml文件
     *
     * @param location fxml文件的路径
     * @param initArgs 自定义初始化参数
     * @return fxml对象
     * @throws IOException 找不到文件或加载失败
     */
    public FXMLView load(URL location, ViewArgs initArgs) throws IOException {
        fxmlLoader.setLocation(location);
        //加载fxml文件 (controller的onCreate方法被回调)
        Parent rootNode = fxmlLoader.load();
        //获取controller对象
        Object controllerObj = fxmlLoader.getController();
        if (!(controllerObj instanceof BaseController controller)) {
            throw new IllegalStateException("Controller object is not an instance of BaseController !");
        }
        //controller初始化回调
        AppContext appContext = AppContext.getInstance();
        controller.onLoadInit(appContext, rootNode, initArgs);
        return new FXMLView(rootNode, controller, location);
    }

    public FXMLView load(Class<?> clazz, String fxmlPath, ViewArgs initArgs) throws IOException, URISyntaxException {
        if (clazz == null) {
            clazz = getClass();
        }
        URL location = clazz.getResource(fxmlPath);
        if (location == null) {
            location = new URI(fxmlPath).toURL();
        }
        return load(location, initArgs);
    }

    public FXMLView load(String fxmlPath, ViewArgs initArgs) throws IOException, URISyntaxException {
        return load(null, fxmlPath, initArgs);
    }

    public FXMLView load(Class<?> clazz, String fxmlPath) throws IOException, URISyntaxException {
        return load(clazz, fxmlPath, null);
    }

    public FXMLView load(String fxmlPath) throws IOException, URISyntaxException {
        return load(null, fxmlPath, null);
    }

}
