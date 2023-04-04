package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

/**
 * @since 2022/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MainViewBean extends BaseConfigBean {

    private String path;
    private transient URL mainViewUrl;

    private String stageStyle;
    private transient StageStyle stageStyleEnum;

    private String draggable;
    private boolean draggableBool = false;

    private String resizable;
    private boolean resizableBool = true;

    public URL getMainViewUrl(Class<?> appClass) {
        if (mainViewUrl == null) {
            mainViewUrl = resolveUrl(path, appClass, "mainViewPath", true);
        }
        return mainViewUrl;
    }

    public void setDraggable(String draggable) {
        this.draggable = draggable;
        if (StringUtils.isNotNull(draggable)) {
            this.draggableBool = (draggable.equalsIgnoreCase("true") || draggable.equalsIgnoreCase("on") || draggable.equals("1"));
        } else {
            this.draggableBool = false;
        }
    }

    public void setDraggableBool(boolean draggableBool) {
        this.draggableBool = draggableBool;
        this.draggable = draggableBool + "";
    }

    public void setResizable(String resizable) {
        this.resizable = resizable;
        if (StringUtils.isNotNull(resizable)) {
            this.resizableBool = (resizable.equalsIgnoreCase("true") || resizable.equalsIgnoreCase("on") || resizable.equals("1"));
        } else {
            this.resizableBool = false;
        }
    }

    public void setResizableBool(boolean resizableBool) {
        this.resizableBool = resizableBool;
        this.resizable = resizableBool + "";
    }


    public StageStyle getStageStyleEnum() {
        if (stageStyleEnum == null && StringUtils.isNotNull(stageStyle)) {
            String name = stageStyle.toUpperCase();
            stageStyleEnum = StageStyle.valueOf(name);
        }
        return stageStyleEnum;
    }

    public void setStageStyleEnum(StageStyle stageStyleEnum) {
        this.stageStyleEnum = stageStyleEnum;
        if (stageStyleEnum != null) {
            stageStyle = stageStyleEnum.name();
        } else {
            stageStyle = null;
        }
    }

}
