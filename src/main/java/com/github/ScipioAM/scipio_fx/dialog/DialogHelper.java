package com.github.ScipioAM.scipio_fx.dialog;

import com.github.ScipioAM.scipio_fx.constant.Language;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.concurrent.Worker;
import javafx.scene.control.ButtonType;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;

import java.util.Optional;

/**
 * 原生+ControlsFX的dialog工具类
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
public class DialogHelper {

    private DialogHelper() {
    }

    //==================================== ↓↓↓↓↓↓ 异常dialog ↓↓↓↓↓↓ ====================================

    public static ExceptionDialog showExceptionDialog(Throwable e, String title, String headerText, String contentText) {
        ExceptionDialog dialog = new ExceptionDialog(e);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.show();
        return dialog;
    }

    public static ExceptionDialog showExceptionDialog(Throwable e, Language language) {
        ExceptionDialog dialog = new ExceptionDialog(e);
        switch (language) {
            case EN :
                dialog.setTitle("Exception");
                dialog.setHeaderText("Exception information");
                dialog.setContentText(StringUtils.isNull(e.getMessage()) ? "Empty message" : e.getMessage());
                break;
            case CN :
                dialog.setTitle("异常");
                dialog.setHeaderText("异常信息");
                dialog.setContentText(StringUtils.isNull(e.getMessage()) ? "无信息" : e.getMessage());
                break;
        }
        dialog.show();
        return dialog;
    }

    public static ExceptionDialog showExceptionDialog(Throwable e) {
        return showExceptionDialog(e, Language.CN);
    }

    //==================================== ↓↓↓↓↓↓ 常规dialog ↓↓↓↓↓↓ ====================================

    public static Optional<ButtonType> showDialog(String title, String headerText, String content) {
        return JFXDialog.buildDefault(title, headerText, content).showAndWait();
    }

    public static Optional<ButtonType> showDialog(String headerText, String content) {
        return JFXDialog.buildDefault(headerText, content).showAndWait();
    }

    public static Optional<ButtonType> showDialog(String content) {
        return JFXDialog.buildDefault(content).showAndWait();
    }

    //==================================== ↓↓↓↓↓↓ 进度加载dialog ↓↓↓↓↓↓ ====================================

    //TODO ProgressDialog还可以进一步封装，否则要在外面启动Worker子线程

    public static ProgressDialog showProgressDialog(String title, String headerText, String contentText, Worker<?> worker) {
        ProgressDialog dialog = new ProgressDialog(worker);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog showProgressDialog(String headerText, String contentText, Worker<?> worker) {
        return showProgressDialog(null, headerText, contentText, worker);
    }

    public static ProgressDialog showProgressDialog(String contentText, Worker<?> worker) {
        return showProgressDialog(null, null, contentText, worker);
    }

}
