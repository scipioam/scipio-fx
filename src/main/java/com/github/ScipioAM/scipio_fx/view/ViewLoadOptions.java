package com.github.ScipioAM.scipio_fx.view;

import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.constant.AppView;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.utils.StageUtil;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * @since 2022/6/22
 */
@Data
@Accessors(chain = true)
public class ViewLoadOptions {

    private URL fxmlUrl;

    private AppView viewInfo;

    private String title;

    private Object initArgs;

    @Setter(AccessLevel.NONE)
    private StageStyle stageStyle;

    @Setter(AccessLevel.NONE)
    private Modality modality;

    @Setter(AccessLevel.NONE)
    private Parent container;

    private ApplicationConfig appConfig;

    public static ViewLoadOptions build() {
        return new ViewLoadOptions();
    }

    public ViewLoadOptions setStageOptions(Parent container, StageStyle stageStyle, Modality modality) {
        this.container = container;
        this.stageStyle = stageStyle;
        this.modality = modality;
        return this;
    }

    public ViewLoadOptions setViewInfo(AppView viewInfo) {
        this.viewInfo = viewInfo;
        if (viewInfo != null) {
            this.title = viewInfo.title();
        }
        return this;
    }

    public ViewLoadOptions setFxmlUrl(Class<?> clazz, String fxmlPath) {
        URL fxmlUrl = clazz.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IllegalStateException("can not get fxmlUrl from class.getResource(), class[" + clazz.getName() + "], fxmlPath[" + fxmlPath + "]");
        }
        this.fxmlUrl = fxmlUrl;
        return this;
    }

    public ViewLoadOptions setFxml(Class<?> clazz, AppView viewInfo) {
        setViewInfo(viewInfo);
        if (viewInfo != null) {
            setFxmlUrl(clazz, viewInfo.fxmlPath());
        }
        return this;
    }

    public ViewLoadOptions defaultStageOptions(Parent container) {
        this.stageStyle = StageStyle.UTILITY;
        this.modality = Modality.APPLICATION_MODAL;
        this.container = container;
        return this;
    }

    /**
     * 是否需要构建Stage
     * @return true:需要
     */
    public boolean isNeedStage() {
        return (stageStyle != null && container != null);
    }

    /**
     * 构建Stage
     */
    public void buildStageForView(FXMLView view) {
        BaseController controller = view.getController();
        Stage stage = StageUtil.buildStage(stageStyle, modality, container.getScene().getWindow(), view.getView(), title);
        view.setStage(stage);
        controller.setParentStage(stage);
    }

}
