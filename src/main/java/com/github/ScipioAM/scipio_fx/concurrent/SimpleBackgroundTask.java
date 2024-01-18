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
        try {
            T data = execute();
            if (finishUiCallback != null) {
                finishUiCallback.setData(data);
            }
        } catch (Exception e) {
            log.error("Background task run failed !", e);
            if (errorUiCallback == null) {
                errorUiCallback = new SimpleUiCallback<>() {
                    @Override
                    public void execute(T data) {
                        AlertHelper.showError("", "错 误", "后台任务执行失败！ " + e);
                    }
                };
            }
            Platform.runLater(errorUiCallback);
        }
        //运行完后的善后
        if (finishUiCallback != null) {
            Platform.runLater(finishUiCallback);
        }
    }

    protected abstract T execute() throws Exception;

}
