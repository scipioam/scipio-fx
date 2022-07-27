package com.github.ScipioAM.scipio_fx.search;

import java.util.List;

/**
 * 搜索的具体实现
 *
 * @since 2022/7/27
 */
@FunctionalInterface
public interface SearchImplementation {

    /**
     * 搜索的具体实现
     *
     * @return 搜索到的数据
     */
    List<?> doSearch();

}
