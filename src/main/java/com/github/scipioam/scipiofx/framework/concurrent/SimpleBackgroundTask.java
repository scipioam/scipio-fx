package com.github.scipioam.scipiofx.framework.concurrent;

import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import javafx.application.Platform;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单后台任务父类
 *
 * @author Alan Scipio
 * created on 2024/1/18
 */
@Setter
public abstract class SimpleBackgroundTask<T> implements Runnable {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected SimpleOnFinishListener<T> onSuccessListener;
    protected SimpleOnFinishListener<T> onFinishListener;
    protected SimpleOnErrorListener onErrorListener;

    @Override
    public void run() {
        T data = null;
        try {
            //运行任务
            data = execute();
            onSuccess(data);
            if (onSuccessListener != null) {
                T finalData = data;
                Platform.runLater(() -> onSuccessListener.handle(finalData));
            }
        } catch (Exception e) {
            //运行失败
            log.error("Background task run failed !", e);
            onError(e);
            if (onErrorListener != null) {
                Platform.runLater(() -> onErrorListener.onError(e));
            }
        }
        //运行完后的善后
        onFinish(data);
        if (onFinishListener != null) {
            T finalData1 = data;
            Platform.runLater(() -> onFinishListener.handle(finalData1));
        }
    }

    protected abstract T execute() throws Exception;

    protected void onSuccess(T data) {
    }

    protected void onError(Throwable e) {
    }

    protected void onFinish(T data) {
    }

    public void setDefaultOnErrorListener() {
        onErrorListener = CFXDialogHelper::showExceptionDialog;
    }

}
