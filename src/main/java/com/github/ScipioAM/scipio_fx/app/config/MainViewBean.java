package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
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

    public URL getMainViewUrl(Class<?> appClass) throws IOException {
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

    public void setStageStyle(String stageStyle) {
        this.stageStyle = stageStyle;
        if (StringUtils.isNotNull(stageStyle)) {
            stageStyleEnum = StageStyle.valueOf(stageStyle);
        } else {
            stageStyleEnum = null;
        }
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
