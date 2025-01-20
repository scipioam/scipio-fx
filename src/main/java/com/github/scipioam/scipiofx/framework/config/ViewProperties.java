package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.utils.StringUtils;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2024/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ViewProperties extends AbstractProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer defaultViewId;

    private String mainViewPath;
    private transient URL mainViewUrl;

    private String stageStyle;

    private String draggable;

    private String resizable;

    public URL getMainViewUrl() {
        return loadUrl(mainViewUrl, mainViewPath);
    }

    public StageStyle getStageStyleEnum() {
        return StringUtils.isNotBlank(stageStyle) ? StageStyle.valueOf(stageStyle.toUpperCase()) : null;
    }

    public boolean isDraggableFlag() {
        return loadBoolean(draggable);
    }

    public boolean isResizableFlag() {
        return loadBoolean(resizable);
    }

    //===================================================================================================

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final ViewProperties prop = new ViewProperties();

        public Builder appClass(Class<? extends JFXApplication> appClass) {
            prop.setAppClass(appClass);
            return this;
        }

        public Builder defaultViewId(Integer defaultViewId) {
            prop.setDefaultViewId(defaultViewId);
            return this;
        }

        public Builder mainViewPath(String mainViewPath) {
            prop.setMainViewPath(mainViewPath);
            return this;
        }

        public Builder stageStyle(String stageStyle) {
            prop.setStageStyle(stageStyle);
            return this;
        }

        public Builder draggable(String draggable) {
            prop.setDraggable(draggable);
            return this;
        }

        public Builder resizable(String resizable) {
            prop.setResizable(resizable);
            return this;
        }

        public ViewProperties build() {
            return prop;
        }
    }
}
