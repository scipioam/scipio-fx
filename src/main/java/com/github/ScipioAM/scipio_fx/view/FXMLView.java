package com.github.ScipioAM.scipio_fx.view;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.dialog.AlertHelper;
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

    private Parent view;
    private BaseController controller;
    private URL fxmlUrl;

    private Stage stage;
    private String title;

    public FXMLView() {
    }

    public FXMLView(Parent view, BaseController controller, URL fxmlUrl) {
        this.view = view;
        this.controller = controller;
        this.fxmlUrl = fxmlUrl;
    }

    /**
     * 显示界面
     */
    public void show(Object... showArgs) {
        if (stage != null) {
            if (showArgs != null && showArgs.length == 1 && showArgs[0] == null) {
                showArgs = null;
            }
            controller.onShow(stage, showArgs);
            stage.show();
        } else {
            throw new IllegalStateException("Stage object is null while call FXMLView.show() !");
        }
    }

    public void show() {
        show((Object[]) null);
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
     * @param view    view对象，为空则构建并返回
     * @param options 加载view的参数
     * @return 加载出来的view
     */
    public static FXMLView load(FXMLView view, ViewLoadOptions options) {
        if (view == null) {
            try {
                view = FXMLViewLoader.build()
                        .load(options.getFxmlUrl(), options.getInitArgs());
                if (options.isNeedStage()) {
                    options.buildStageForView(view);
                }
            } catch (Exception e) {
                AlertHelper.showError("程序错误", "打开界面失败", "请联系管理员");
                e.printStackTrace();
                return null;
            }
        }
        return view;
    }

    public static FXMLView load(ViewLoadOptions options) {
        return load(null, options);
    }

    /**
     * 如果view是空壳则加载，然后显示
     *
     * @param view    view对象（第一次时应当是个实例化的空壳）
     * @param options 加载view的参数
     */
    public static void loadAndShow(FXMLView view, ViewLoadOptions options) {
        if (view == null) {
            throw new IllegalArgumentException("view can not be null");
        }
        Stage stage = view.getStage();
        if (stage == null) {
            try {
                FXMLView newView = FXMLViewLoader.build()
                        .load(options.getFxmlUrl(), options.getInitArgs());
                view.setView(newView.getView())
                        .setFxmlUrl(newView.getFxmlUrl())
                        .setTitle(newView.getTitle())
                        .setController(newView.controller);
                options.buildStageForView(view);
            } catch (Exception e) {
                AlertHelper.showError("程序错误", "打开界面失败", "请联系管理员");
                e.printStackTrace();
            }
        }
        view.show();
    }

}
