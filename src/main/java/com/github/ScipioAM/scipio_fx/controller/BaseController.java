package com.github.ScipioAM.scipio_fx.controller;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alan Scipio
 * @since 1.00 _ 2020/9/14
 */
@Data
public abstract class BaseController implements Initializable {

    protected Stage thisStage;

    //=========================================== ↓↓↓↓↓↓ controller生命周期 ↓↓↓↓↓↓ ===========================================

    /**
     * controller被创建时的回调，早于{@link #onLoadInit}
     *
     * @param location  fxml文件的位置
     * @param resources fxml root object
     */
    public void onCreate(URL location, ResourceBundle resources) {
        registerListener();
    }

    /**
     * controller初始化回调，晚于{@link #onCreate}
     *
     * @param rootNode 本view的根节点
     * @param initArg  初始化时带的参数，可能为null
     */
    public void onLoadInit(Parent rootNode, Object initArg) {
    }

    /**
     * 显示时的回调
     *
     * @param thisStage 本界面所从属的stage
     */
    public void onShow(Stage thisStage) {
    }

    /**
     * 程序停止时的回调
     */
    public void onStop() throws Exception {
        unregisterListener();
    }

    //=========================================== ↓↓↓↓↓↓ 辅助性回调（本身无意义） ↓↓↓↓↓↓ ===========================================

    /**
     * 注册监听器
     */
    public void registerListener() {
    }

    /**
     * 注销监听器，避免内存泄露
     */
    public void unregisterListener() {
    }

    //=========================================== ↓↓↓↓↓↓ 不需要重写的 ↓↓↓↓↓↓ ===========================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onCreate(location, resources);
    }

}
