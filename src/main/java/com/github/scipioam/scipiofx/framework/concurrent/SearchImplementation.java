package com.github.scipioam.scipiofx.framework.concurrent;

import java.util.List;

/**
 * 搜索的具体实现
 *
 * @since 2022/7/27
 */
@FunctionalInterface
public interface SearchImplementation<T> {

    /**
     * 搜索的具体实现
     *
     * @return 搜索到的数据
     */
    List<T> doSearch();

}
