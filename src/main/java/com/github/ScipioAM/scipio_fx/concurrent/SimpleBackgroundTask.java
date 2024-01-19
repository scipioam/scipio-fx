package com.github.ScipioAM.scipio_fx.concurrent;

import com.github.ScipioAM.scipio_fx.dialog.AlertHelper;
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

    protected SimpleUiCallback<T> finishUiCallback;
    protected SimpleUiCallback<T> errorUiCallback;

    @Override
    public void run() {
        T data = null;
        try {
            //运行任务
            data = execute();
        } catch (Exception e) {
            //运行失败
            log.error("Background task run failed !", e);
            onError(e);
            if (errorUiCallback != null) {
                Platform.runLater(errorUiCallback);
            }
        }
        //运行完后的善后
        onFinish(data);
        if (finishUiCallback != null) {
            finishUiCallback.setData(data);
            Platform.runLater(finishUiCallback);
        }
    }

    protected abstract T execute() throws Exception;

    protected void onError(Throwable e) {
    }

    protected void onFinish(T data) {
    }

    public void setDefaultErrorUiCallback() {
        errorUiCallback = new SimpleUiCallback<>() {
            @Override
            public void execute(T data) {
                AlertHelper.showError("", "执行结果", "后台任务执行失败！");
            }
        };
    }

}
