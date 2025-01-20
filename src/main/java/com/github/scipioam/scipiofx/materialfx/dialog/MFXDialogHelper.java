package com.github.scipioam.scipiofx.materialfx.dialog;

import com.github.scipioam.scipiofx.framework.Language;
import com.github.scipioam.scipiofx.view.dialog.DialogBtnListener;
import javafx.scene.layout.Pane;

/**
 * {@link MFXDialog}的工具类
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
public class MFXDialogHelper {

    private MFXDialogHelper() {
    }

    /**
     * 构建并显示对话框
     *
     * @param ownerNode   [可选]对话框的父节点
     * @param type        对话框类型
     * @param headerText  标题文本
     * @param contentText 内容文本
     * @param language    按钮语言
     * @param okBtnAction ok按钮的动作回调
     * @return 构建好的对话框对象
     */
    public static MFXDialog showDialog(Pane ownerNode, MFXDialogType type, String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        MFXDialog dialog = new MFXDialog(type, ownerNode)
                .setHeaderText(headerText)
                .setContentText(contentText);
        if (okBtnAction != null) {
            dialog.addOkButton(okBtnAction, language).addCancelButton(language);
        } else {
            dialog.addOkButton(language);
        }
        dialog.show();
        return dialog;
    }

    public static MFXDialog showInfo(Pane ownerNode, String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(ownerNode, MFXDialogType.INFO, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showInfo(String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(null, MFXDialogType.INFO, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showInfo(String headerText, String contentText, Language language) {
        return showInfo(headerText, contentText, language, null);
    }

    public static MFXDialog showInfo(Pane ownerNode, String headerText, String contentText) {
        return showInfo(ownerNode, headerText, contentText, Language.ENGLISH, null);
    }

    public static MFXDialog showInfo(String headerText, String contentText) {
        return showInfo(headerText, contentText, Language.ENGLISH, null);
    }

    //=================================================================================================================================================================

    public static MFXDialog showWarning(Pane ownerNode, String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(ownerNode, MFXDialogType.WARN, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showWarning(String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(null, MFXDialogType.WARN, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showWarning(String headerText, String contentText, Language language) {
        return showWarning(headerText, contentText, language, null);
    }

    public static MFXDialog showWarning(Pane ownerNode, String headerText, String contentText) {
        return showWarning(ownerNode, headerText, contentText, Language.ENGLISH, null);
    }

    public static MFXDialog showWarning(String headerText, String contentText) {
        return showWarning(headerText, contentText, Language.ENGLISH, null);
    }

    //=================================================================================================================================================================

    public static MFXDialog showError(Pane ownerNode, String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(ownerNode, MFXDialogType.ERROR, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showError(String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(null, MFXDialogType.ERROR, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showError(String headerText, String contentText, Language language) {
        return showError(headerText, contentText, language, null);
    }

    public static MFXDialog showError(Pane ownerNode, String headerText, String contentText) {
        return showError(ownerNode, headerText, contentText, Language.ENGLISH, null);
    }

    public static MFXDialog showError(String headerText, String contentText) {
        return showError(headerText, contentText, Language.ENGLISH, null);
    }
}
