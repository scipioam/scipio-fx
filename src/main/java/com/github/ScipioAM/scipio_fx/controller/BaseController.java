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

    protected Stage parentStage;

    //=========================================== ↓↓↓↓↓↓ controller生命周期 ↓↓↓↓↓↓ ===========================================

    /**
     * controller被创建时的回调，早于{@link #onLoadInit}
     *
     * @param location  fxml文件的位置
     * @param resources fxml root object
     */
    public void onCreate(URL location, ResourceBundle resources) {
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
     * 初始化子线程结束时的回调
     * （未配置初始化子线程则不会被调用）
     */
    public void onInitThreadFinished() {
    }

    /**
     * 程序停止时的回调
     */
    public void onStop() throws Exception {
    }

    //=========================================== ↓↓↓↓↓↓ 不需要重写的 ↓↓↓↓↓↓ ===========================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onCreate(location, resources);
    }

}
