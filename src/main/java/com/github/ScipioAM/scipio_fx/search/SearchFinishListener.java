package com.github.ScipioAM.scipio_fx.search;

import java.util.List;

/**
 * 搜索完成的回调监听
 *
 * @since 2022/7/27
 */
@FunctionalInterface
public interface SearchFinishListener {

    /**
     * 搜索完成的回调监听
     *
     * @param data 搜索到的数据。如果搜索出错则为null
     */
    void searchFinished(List<?> data);

}
