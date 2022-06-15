package com.github.ScipioAM.scipio_fx.view;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.dialog.AlertHelper;
import com.github.ScipioAM.scipio_fx.utils.StageUtil;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@Accessors(chain = true)
@Data
public class FXMLView {

    private Parent view;
    private BaseController controller;
    private String fxmlPath;

    private Stage stage;
    private String title;

    public FXMLView() {
    }

    public FXMLView(Parent view, BaseController controller, String fxmlPath) {
        this.view = view;
        this.controller = controller;
        this.fxmlPath = fxmlPath;
    }

    /**
     * 显示界面
     */
    public void show() {
        if (stage != null) {
            controller.onShow(stage);
            stage.show();
        } else {
            System.err.println("Stage object is null while call FXMLView.show() !");
        }
    }

    /**
     * 关闭界面
     */
    public void close() {
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Stage object is null while call FXMLView.close() !");
        }
    }

    //=================================================================================================================

    /**
     * 加载view
     *
     * @param fxmlPath   fxml文件路径
     * @param title      显示窗口的标题
     * @param container  从属于的容器（背后的window）
     * @param stageStyle stage式样
     * @param modality   模态框式样
     * @param initArg    自定义初始化参数
     * @return 加载出来的view
     */
    public static FXMLView load(String fxmlPath,
                                String title,
                                Parent container,
                                StageStyle stageStyle,
                                Modality modality,
                                Object initArg) {
        FXMLView view;
        try {
            view = FXMLViewLoader.build().load(fxmlPath, initArg);
        } catch (Exception e) {
            AlertHelper.showError("程序错误", "打开界面失败", "请联系管理员");
            e.printStackTrace();
            return null;
        }

        BaseController controller = view.getController();
        Stage stage = StageUtil.buildStage(stageStyle, modality, container.getScene().getWindow(), view.getView(), title);
        view.setStage(stage);
        controller.setThisStage(stage);
        return view;
    }

    public static FXMLView load(String fxmlPath, String title, Parent container, Object initArg) {
        return load(fxmlPath, title, container, StageStyle.UTILITY, Modality.APPLICATION_MODAL, initArg);
    }

    public static FXMLView load(String fxmlPath, String title, Parent container) {
        return load(fxmlPath, title, container, StageStyle.UTILITY, Modality.APPLICATION_MODAL, null);
    }

    /**
     * 如果view是空壳则加载，然后显示
     *
     * @param view       view对象（第一次时应当是个实例化的空壳）
     * @param fxmlPath   fxml文件路径
     * @param title      显示窗口的标题
     * @param container  从属于的容器（背后的window）
     * @param stageStyle stage式样
     * @param modality   模态框式样
     * @param initArg    自定义初始化参数
     */
    public static void loadAndShow(FXMLView view,
                                   String fxmlPath,
                                   String title,
                                   Parent container,
                                   StageStyle stageStyle,
                                   Modality modality,
                                   Object initArg) {
        if (view == null) {
            throw new IllegalArgumentException("view can not be null");
        }
        Stage stage = view.getStage();
        if (stage == null) {
            FXMLView loadedView = load(fxmlPath, title, container, stageStyle, modality, initArg);
            if (loadedView != null) {
                view.setView(loadedView.getView())
                        .setFxmlPath(fxmlPath)
                        .setController(loadedView.getController())
                        .setStage(loadedView.getStage())
                        .setTitle(title);

            } else {
                throw new IllegalStateException("Load new FXMLView failed!");
            }
        }
        view.show();
    }

    public static void loadAndShow(FXMLView view, String fxmlPath, String title, Parent container, Object initArg) {
        loadAndShow(view, fxmlPath, title, container, StageStyle.UTILITY, Modality.APPLICATION_MODAL, initArg);
    }

    public static void loadAndShow(FXMLView view, String fxmlPath, String title, Parent container) {
        loadAndShow(view, fxmlPath, title, container, StageStyle.UTILITY, Modality.APPLICATION_MODAL, null);
    }

}
