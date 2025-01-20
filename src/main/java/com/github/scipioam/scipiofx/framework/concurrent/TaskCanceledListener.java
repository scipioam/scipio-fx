package com.github.scipioam.scipiofx.framework.concurrent;

/**
 * @since 2022/9/19
 */
@FunctionalInterface
public interface TaskCanceledListener {

    void onCanceled(AppAbstractTask<?> context);

}
