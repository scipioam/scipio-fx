package com.github.ScipioAM.scipio_fx.search;

import javafx.application.Platform;

import java.util.List;

/**
 * 搜索子线程。用于避免耗时的搜索操作阻塞住UI线程
 *
 * @since 2022/7/27
 */
public class SearchThread implements Runnable {

    private final SearchImplementation searchImpl;
    private final SearchFinishListener finishListener;
    private final SearchErrorListener errorListener;

    public SearchThread(SearchImplementation searchImpl, SearchFinishListener finishListener, SearchErrorListener errorListener) {
        this.searchImpl = searchImpl;
        this.finishListener = finishListener;
        this.errorListener = errorListener;
    }

    public SearchThread(SearchImplementation searchImpl, SearchFinishListener finishListener) {
        this(searchImpl, finishListener, SearchErrorListener.PRINTER);
    }

    @Override
    public void run() {
        //执行搜索
        List<?> data = null;
        try {
            data = searchImpl.doSearch();
        } catch (Exception e) {
            errorListener.onError(e, searchImpl);
        }
        //搜索完成后的数据回显
        final List<?> finalData = data;
        Platform.runLater(() -> finishListener.searchFinished(finalData));
    }

}
