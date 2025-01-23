package com.github.scipioam.scipiofx.framework.fxml;

import com.github.scipioam.scipiofx.framework.BaseController;
import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@SuppressWarnings("LombokGetterMayBeUsed")
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
            CFXDialogHelper.showExceptionDialog(e);
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public FXMLView setTitle(String title) {
        this.title = title;
        return this;
    }

    public Stage getStage() {
        return stage;
    }

    public FXMLView setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public BaseController getController() {
        return controller;
    }

    public FXMLView setController(BaseController controller) {
        this.controller = controller;
        return this;
    }

    public Parent getSelf() {
        return self;
    }

    public FXMLView setSelf(Parent self) {
        this.self = self;
        return this;
    }

    public URL getFxmlUrl() {
        return fxmlUrl;
    }

    public FXMLView setFxmlUrl(URL fxmlUrl) {
        this.fxmlUrl = fxmlUrl;
        return this;
    }
}
