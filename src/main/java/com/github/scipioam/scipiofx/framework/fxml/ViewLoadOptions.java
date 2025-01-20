package com.github.scipioam.scipiofx.framework.fxml;

import com.github.scipioam.scipiofx.framework.AppViewId;
import com.github.scipioam.scipiofx.utils.StageUtil;
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

    private AppViewId viewId;

    private String title;

    @Setter(AccessLevel.NONE)
    private ViewArgs initArgs;

    @Setter(AccessLevel.NONE)
    private StageStyle stageStyle;

    @Setter(AccessLevel.NONE)
    private Modality modality;

    @Setter(AccessLevel.NONE)
    private Parent container;

    public static ViewLoadOptions build() {
        return new ViewLoadOptions();
    }

    public ViewLoadOptions setStageOptions(Parent container, StageStyle stageStyle, Modality modality) {
        this.container = container;
        this.stageStyle = stageStyle;
        this.modality = modality;
        return this;
    }

    public ViewLoadOptions setViewId(AppViewId viewId) {
        this.viewId = viewId;
        if (viewId != null) {
            this.title = viewId.title();
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

    public ViewLoadOptions setFxml(Class<?> clazz, AppViewId viewInfo) {
        setViewId(viewInfo);
        if (viewInfo != null) {
            setFxmlUrl(clazz, viewInfo.fxmlPath());
        }
        return this;
    }

    public ViewLoadOptions setFxml(AppViewId viewInfo) {
        setViewId(viewInfo);
        if (viewInfo != null) {
            setFxmlUrl(viewInfo.getClass(), viewInfo.fxmlPath());
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
     *
     * @return true:需要
     */
    public boolean isNeedStage() {
        return (stageStyle != null && container != null);
    }

    /**
     * 构建Stage
     */
    public void buildStageForView(FXMLView view) {
        Stage stage = StageUtil.buildStage(stageStyle, modality, container.getScene().getWindow(), view.getSelf(), title);
        view.setStage(stage);
    }

    public ViewLoadOptions setInitArgs(ViewArgs initArgs) {
        this.initArgs = initArgs;
        return this;
    }

    public ViewLoadOptions setInitArg(String key, Object initArg) {
        if (initArg != null) {
            this.initArgs = ViewArgs.create().setArg(key, initArg);
        } else {
            this.initArgs = null;
        }
        return this;
    }

}
