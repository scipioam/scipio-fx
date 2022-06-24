package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.exception.ResourceException;
import javafx.scene.image.Image;

import java.net.URL;

/**
 * @since 2022/6/24
 */
public class ResourceManage {

    /**
     * 获取资源文件url
     *
     * @param url url路径
     * @return url对象
     * @throws ResourceException url不对或资源不存在
     */
    public static URL getResourceUrl(String url) throws ResourceException {
        Class<?> appClass = JFXApplication.context.getAppClass();
        URL resUrl = appClass.getResource(url);
        if (resUrl == null) {
            throw new ResourceException("Invalid URL or resource not found");
        }
        return resUrl;
    }

    /**
     * 获取图片对象
     *
     * @param url url路径
     * @return 图片对象
     */
    public static Image getImage(String url) throws ResourceException {
        URL imageUrl = getResourceUrl(url);
        return new Image(imageUrl.toExternalForm());
    }

}
