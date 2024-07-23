package com.github.ScipioAM.scipio_fx.concurrent;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.slf4j.Logger;

/**
 * JavaFX后台子线程的共通父类
 *
 * @since 2022/9/16
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public abstract class AppAbstractTask<T> extends Task<T> implements AppBackgroundThread<T> {

    @Setter
    protected Logger LOG;

    /**
     * 绑定的message属性
     *
     * @see #updateMessage(String)
     */
    protected StringProperty uiMessage;

    /**
     * 绑定的progress属性
     *
     * @see #updateProgress(double, double)
     * @see #updateProgress(long, long)
     */
    protected DoubleProperty uiProgress;

    /**
     * 绑定的title属性
     *
     * @see #updateTitle(String)
     */
    protected StringProperty uiTitle;

    /**
     * 绑定的value属性
     *
     * @see #updateValue(Object)
     */
    protected ObjectProperty<T> uiValue;

    @Setter
    protected TaskSucceedListener<T> succeedListener;
    @Setter
    protected TaskFailureListener failureListener;
    @Setter
    protected TaskDoneListener doneListener;
    @Setter
    protected TaskCanceledListener canceledListener;

    //=========================================== ↓↓↓↓↓↓ 构造方法 ↓↓↓↓↓↓ ===========================================

    public AppAbstractTask() {
        this(null, null);
    }

    public AppAbstractTask(StringProperty uiMessage) {
        this(uiMessage, null);
    }

    public AppAbstractTask(DoubleProperty uiProgress) {
        this(null, uiProgress);
    }

    public AppAbstractTask(StringProperty uiMessage, DoubleProperty uiProgress) {
        this.uiMessage = uiMessage;
        this.uiProgress = uiProgress;
        bind();
    }

    //=========================================== ↓↓↓↓↓↓ API及主要实现 ↓↓↓↓↓↓ ===========================================

    /**
     * {@link Task}原本的call
     *
     * @return 线程执行完异步返回的结果
     */
    @Override
    public T call() throws Exception {
        return doCall();
    }

    /**
     * 获取本线程执行结果
     *
     * @return {@link #call()}的返回值
     */
    @Override
    public T getTaskResult() {
        try {
            if (isDone()) {
                //执行完毕则返回执行结果
                return get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //没有执行完毕，或者发生异常，则返回null
        return null;
    }

    /**
     * UI的绑定
     */
    public void bind() {
        if (uiMessage != null) {
            uiMessage.bind(this.messageProperty());
        }
        if (uiProgress != null) {
            uiProgress.bind(this.progressProperty());
        }
        if (uiTitle != null) {
            uiTitle.bind(this.titleProperty());
        }
        if (uiValue != null) {
            uiValue.bind(this.valueProperty());
        }
    }

    //=========================================== ↓↓↓↓↓↓ Task状态回调 ↓↓↓↓↓↓ ===========================================

    /**
     * 运行在子线程，无论什么状态，必然执行的（先执行此done，再执行succeeded,failed,canceled这三个生命周期）
     */
    @Override
    protected void done() {
        //UI的解绑
        if (uiMessage != null && uiMessage.isBound()) {
            uiMessage.unbind();
        }
        if (uiProgress != null && uiProgress.isBound()) {
            uiProgress.unbind();
        }
        if (uiTitle != null && uiTitle.isBound()) {
            uiTitle.unbind();
        }
        if (uiValue != null && uiValue.isBound()) {
            uiValue.unbind();
        }
        if (doneListener != null) {
            Platform.runLater(() -> doneListener.onDone(this));
        }
    }

    /**
     * 运行在FX主线程，任务成功完成时调用
     */
    @Override
    protected void succeeded() {
        if (succeedListener != null) {
            T result = getTaskResult();
            succeedListener.onSucceed(this, result);
        }
    }

    /**
     * 运行在FX主线程，任务失败时调用
     */
    @Override
    protected void failed() {
        if (getException() != null) {
            if (LOG != null) {
                LOG.error("Background task running failed, {}", getException().toString());
            }
            getException().printStackTrace();
        }
        if (failureListener != null) {
            failureListener.onFailure(this, this.getException());
        }
    }

    /**
     * 运行在FX主线程，任务被取消时调用
     */
    @Override
    protected void cancelled() {
        if (canceledListener != null) {
            canceledListener.onCanceled(this);
        }
    }

    //=========================================== ↓↓↓↓↓↓ UI相关 ↓↓↓↓↓↓ ===========================================

    public void updateUiMessage(final String msg) {
        if (uiMessage != null) {
            Platform.runLater(() -> uiMessage.setValue(msg));
        }
    }

    public void updateUiProgress(final double progress) {
        if (uiProgress != null) {
            Platform.runLater(() -> uiProgress.setValue(progress));
        }
    }

    public void updateUiTitle(final String title) {
        if (uiTitle != null) {
            Platform.runLater(() -> uiTitle.setValue(title));
        }
    }

    public void updateUiValue(final T value) {
        if (uiValue != null) {
            Platform.runLater(() -> uiValue.setValue(value));
        }
    }

    public AppAbstractTask<T> setUiMessage(String uiMessage) {
        this.uiMessage.set(uiMessage);
        bind();
        return this;
    }

    public AppAbstractTask<T> setUiProgress(double uiProgress) {
        this.uiProgress.set(uiProgress);
        bind();
        return this;
    }

    public AppAbstractTask<T> setUiTitle(String uiTitle) {
        this.uiTitle.set(uiTitle);
        bind();
        return this;
    }

    public AppAbstractTask<T> setUiValue(T uiValue) {
        this.uiValue.set(uiValue);
        bind();
        return this;
    }

}
