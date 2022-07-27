package com.github.ScipioAM.scipio_fx.search;

/**
 * 搜索失败的监回调
 *
 * @since 2022/7/27
 */
@FunctionalInterface
public interface SearchErrorListener {

    /**
     * 搜索失败的监回调
     *
     * @param exception  异常处理
     * @param searchImpl 搜索的实现实例
     */
    void onError(Throwable exception, SearchImplementation searchImpl);

    SearchErrorListener PRINTER = (e, searchImpl) -> e.printStackTrace();

}
