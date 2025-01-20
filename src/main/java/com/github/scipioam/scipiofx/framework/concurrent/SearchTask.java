package com.github.scipioam.scipiofx.framework.concurrent;

import javafx.application.Platform;

import java.util.List;

/**
 * 搜索子线程。用于避免耗时的搜索操作阻塞住UI线程
 *
 * @param <T> 搜索的数据类型
 * @since 2022/7/27
 */
public class SearchTask<T> extends AppAbstractTask<List<T>> implements AppBackgroundThread<List<T>> {

    private final SearchImplementation<T> searchImpl;
    private SearchFinishListener<T> finishListener;
    private SearchErrorListener errorListener;

    public SearchTask(SearchImplementation<T> searchImpl) {
        this.searchImpl = searchImpl;
    }

    public SearchTask(SearchImplementation<T> searchImpl, SearchFinishListener<T> finishListener) {
        this(searchImpl, finishListener, SearchErrorListener.PRINTER);
    }

    public SearchTask(SearchImplementation<T> searchImpl, SearchFinishListener<T> finishListener, SearchErrorListener errorListener) {
        this.searchImpl = searchImpl;
        this.finishListener = finishListener;
        this.errorListener = errorListener;
    }

    @Override
    public List<T> doCall() {
        //执行搜索
        List<T> data = searchImpl.doSearch();
        //搜索完成后的数据回显
        final List<T> finalData = data;
        Platform.runLater(() -> finishListener.searchFinished(finalData));
        return data;
    }

    @Override
    protected void failed() {
        super.failed();
        errorListener.onError(getException(), searchImpl);
    }

    public SearchTask<T> setFinishListener(SearchFinishListener<T> finishListener) {
        this.finishListener = finishListener;
        return this;
    }

    public SearchTask<T> setErrorListener(SearchErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    @Override
    public SearchTask<T> setUiMessage(String uiMessage) {
        return (SearchTask<T>) super.setUiMessage(uiMessage);
    }

    @Override
    public SearchTask<T> setUiProgress(double uiProgress) {
        return (SearchTask<T>) super.setUiProgress(uiProgress);
    }

    @Override
    public SearchTask<T> setUiTitle(String uiTitle) {
        return (SearchTask<T>) super.setUiTitle(uiTitle);
    }

    @Override
    public SearchTask<T> setUiValue(List<T> uiValue) {
        return (SearchTask<T>) super.setUiValue(uiValue);
    }

}
