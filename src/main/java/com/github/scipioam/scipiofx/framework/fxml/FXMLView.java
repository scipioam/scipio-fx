package com.github.scipioam.scipiofx.framework.fxml;

import com.github.scipioam.scipiofx.framework.BaseController;
import com.github.scipioam.scipiofx.view.dialog.DialogHelper;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@Accessors(chain = true)
@Data
public class FXMLView {

    private URL fxmlUrl;

    private Parent self;

    private BaseController controller;

    private Stage stage;

    private String title;

    public FXMLView() {
    }

    public FXMLView(Parent self, BaseController controller, URL fxmlUrl) {
        this.self = self;
        this.controller = controller;
        this.fxmlUrl = fxmlUrl;
    }

    /**
     * 显示界面
     */
    public void show(ViewArgs showArgs) {
        if (stage != null) {
            if (showArgs != null && showArgs.isEmpty()) {
                //如果没有设置有意义的参数，则传入null
                showArgs = null;
            }
            controller.onShow(stage, showArgs);
            stage.show();
        } else {
            throw new IllegalStateException("Stage object is null while call FXMLView.show() !");
        }
    }

    public void show() {
        show(null);
    }

    /**
     * 关闭界面
     */
    public void close() {
        if (stage != null) {
            stage.close();
        } else {
            throw new IllegalStateException("Stage object is null while call FXMLView.close() !");
        }
    }

    //=================================================================================================================

    /**
     * 加载view
     *
     * @param options 加载view的参数
     * @return 加载出来的view
     */
    public static FXMLView load(ViewLoadOptions options) {
        try {
            FXMLViewLoader fxmlViewLoader = new FXMLViewLoader();
            FXMLView view = fxmlViewLoader.load(options.getFxmlUrl(), options.getInitArgs());
            if (options.isNeedStage()) {
                options.buildStageForView(view);
            }
            return view;
        } catch (Exception e) {
            DialogHelper.showExceptionDialog(e);
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 如果view是空壳则加载，然后显示
     *
     * @param view    view对象（第一次时应当是个实例化的空壳）
     * @param options 加载view的参数
     */
//    public static void loadAndShow(FXMLView view, ViewLoadOptions options) {
//        if (view == null) {
//            throw new IllegalArgumentException("view can not be null");
//        }
//        Stage stage = view.getStage();
//        if (stage == null) {
//            try {
//                FXMLView newView = FXMLViewLoader.build()
//                        .load(options.getFxmlUrl(), options.getInitArgs());
//                view.setView(newView.getView())
//                        .setFxmlUrl(newView.getFxmlUrl())
//                        .setTitle(newView.getTitle())
//                        .setController(newView.controller);
//                options.buildStageForView(view);
//            } catch (Exception e) {
//                AlertHelper.showError("程序错误", "打开界面失败", "请联系管理员");
//                e.printStackTrace();
//            }
//        }
//        view.show();
//    }

}
