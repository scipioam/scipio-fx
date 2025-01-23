package com.github.scipioam.scipiofx.controlsfx;

import com.github.scipioam.scipiofx.framework.Language;
import com.github.scipioam.scipiofx.utils.StringUtils;
import com.github.scipioam.scipiofx.view.dialog.DialogBtnListener;
import com.github.scipioam.scipiofx.view.dialog.DialogHelper;
import javafx.concurrent.Worker;
import javafx.scene.control.ButtonType;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;

import java.util.Optional;

/**
 * ControlsFX的dialog工具类
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
@SuppressWarnings("UnusedReturnValue")
public final class CFXDialogHelper extends DialogHelper {

    private CFXDialogHelper() {
    }

    /**
     * 构建异常信息对话框
     *
     * @param e        异常
     * @param language 语言
     * @return 异常对话框
     */
    public static ExceptionDialog buildExceptionDialog(Throwable e, Language language) {
        ExceptionDialog dialog = new ExceptionDialog(e);
        String title, headerText, emptyMsg;
        switch (language) {
            case CHINESE:
                title = "异常";
                headerText = "异常信息";
                emptyMsg = "无信息";
                break;
            case ENGLISH:
            default:
                title = "Exception";
                headerText = "Exception information";
                emptyMsg = "Empty message";
                break;
        }
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(StringUtils.isBlank(e.getMessage()) ? emptyMsg : e.getMessage());
        return dialog;
    }

    public static ExceptionDialog showExceptionDialog(Throwable e, Language language) {
        ExceptionDialog dialog = buildExceptionDialog(e, language);
        dialog.show();
        return dialog;
    }

    public static ExceptionDialog showExceptionDialog(Throwable e) {
        return showExceptionDialog(e, Language.CHINESE);
    }

    public static Optional<ButtonType> showExceptionDialogHandle(Throwable e, Language language) {
        ExceptionDialog dialog = buildExceptionDialog(e, language);
        return dialog.showAndWait();
    }

    public static Optional<ButtonType> showExceptionDialogHandle(Throwable e) {
        return showExceptionDialogHandle(e, Language.CHINESE);
    }

    public static void showExceptionDialogHandle(Throwable e, Language language, DialogBtnListener listener) {
        ExceptionDialog dialog = buildExceptionDialog(e, language);
        Optional<ButtonType> handle = dialog.showAndWait();
        if (listener != null && handle.isPresent() && handle.get() != ButtonType.CLOSE) {
            listener.onClicked(null, null);
        }
    }

    public static void showExceptionDialogHandle(Throwable e, DialogBtnListener listener) {
        showExceptionDialogHandle(e, Language.CHINESE, listener);
    }

    //TODO ProgressDialog还可以进一步封装，否则要在外面启动Worker子线程

    /**
     * 显示进度对话框
     *
     * @param title       标题
     * @param headerText  头部文本
     * @param contentText 内容文本
     * @param worker      Worker子线程
     * @return 进度对话框
     */
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
