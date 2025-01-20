package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alan Scipio
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public abstract class BaseController implements Initializable {

    // 只有子view才有值
    protected BaseController parentController;

    public BaseController getParentController() {
        return parentController;
    }

    public void setParentController(BaseController parentController) {
        this.parentController = parentController;
    }

    //=========================================== Controller life circle ===========================================

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
     * @param context  程序上下文
     * @param rootNode 本view的根节点
     * @param initArgs 初始化时的自定义参数，可以为null
     */
    public void onLoadInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
    }

    /**
     * 显示时的回调
     *
     * @param thisStage 本界面所从属的stage
     * @param showArgs  显示时的自定义参数，可以为null
     */
    public void onShow(Stage thisStage, ViewArgs showArgs) {
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

    //=========================================== No need to override ===========================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onCreate(location, resources);
    }

}
