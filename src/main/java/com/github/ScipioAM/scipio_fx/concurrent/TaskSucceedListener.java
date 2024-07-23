package com.github.ScipioAM.scipio_fx.concurrent;

/**
 * @since 2022/9/16
 */
@FunctionalInterface
public interface TaskSucceedListener<T> {

    void onSucceed(AppAbstractTask<T> context, T result);

}
