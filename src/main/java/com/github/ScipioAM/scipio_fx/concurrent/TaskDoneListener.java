package com.github.ScipioAM.scipio_fx.concurrent;

/**
 * @since 2022/9/19
 */
@FunctionalInterface
public interface TaskDoneListener {

    void onDone(AppAbstractTask<?> context);

}
