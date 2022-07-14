package com.github.ScipioAM.scipio_fx.dialog.mfx;

import com.github.ScipioAM.scipio_fx.constant.Language;
import com.github.ScipioAM.scipio_fx.dialog.DialogBtnListener;

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
     * @param type        对话框类型
     * @param headerText  标题文本
     * @param contentText 内容文本
     * @param language    按钮语言
     * @param okBtnAction ok按钮的动作回调
     * @return 构建好的对话框对象
     */
    public static MFXDialog showDialog(MFXDialogType type, String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        MFXDialog dialog = new MFXDialog(type)
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

    public static MFXDialog showInfo(String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(MFXDialogType.INFO, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showInfo(String headerText, String contentText, Language language) {
        return showInfo(headerText, contentText, language, null);
    }

    public static MFXDialog showInfo(String headerText, String contentText) {
        return showInfo(headerText, contentText, Language.EN, null);
    }

    public static MFXDialog showWarn(String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(MFXDialogType.WARN, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showWarn(String headerText, String contentText, Language language) {
        return showWarn(headerText, contentText, language, null);
    }

    public static MFXDialog showWarn(String headerText, String contentText) {
        return showWarn(headerText, contentText, Language.EN, null);
    }

    public static MFXDialog showError(String headerText, String contentText, Language language, DialogBtnListener okBtnAction) {
        return showDialog(MFXDialogType.ERROR, headerText, contentText, language, okBtnAction);
    }

    public static MFXDialog showError(String headerText, String contentText, Language language) {
        return showError(headerText, contentText, language, null);
    }

    public static MFXDialog showError(String headerText, String contentText) {
        return showError(headerText, contentText, Language.EN, null);
    }
}
