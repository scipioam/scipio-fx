package com.github.ScipioAM.scipio_fx.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
public class StageUtil {

    private StageUtil() {
    }

    public static Stage buildStage(Window owner, Parent rootNode) {
        return buildStage(StageStyle.DECORATED, Modality.NONE, owner, rootNode, null);
    }

    public static Stage buildStage(Window owner, Parent rootNode, String title) {
        return buildStage(StageStyle.DECORATED, Modality.NONE, owner, rootNode, title);
    }

    public static Stage buildStage(Modality modality, Window owner, Parent rootNode, String title) {
        return buildStage(StageStyle.DECORATED, modality, owner, rootNode, title);
    }

    public static Stage buildStage(StageStyle stageStyle, Window owner, Parent rootNode, String title) {
        return buildStage(stageStyle, Modality.NONE, owner, rootNode, title);
    }

    /**
     * 构建新界面（new Stage）
     *
     * @param stageStyle 界面样式
     * @param modality   模组样式（是否阻塞父界面等）
     * @param owner      父界面
     * @param rootNode   新界面本身
     * @param title      新界面的标题
     * @return 新界面对象
     */
    public static Stage buildStage(StageStyle stageStyle, Modality modality, Window owner, Parent rootNode, String title) {
        if (rootNode == null) {
            return null;
        }
        Stage stage = new Stage(stageStyle);
        stage.initModality(modality);
        stage.initOwner(owner);
        stage.setScene(new Scene(rootNode));
        stage.setTitle(title);
        return stage;
    }

}
