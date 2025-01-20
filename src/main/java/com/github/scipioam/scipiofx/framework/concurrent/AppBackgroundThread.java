package com.github.scipioam.scipiofx.framework.concurrent;

/**
 * 后台子线程统一接口
 *
 * @param <T> 线程执行完的结果
 * @since 2022/9/16
 */
public interface AppBackgroundThread<T> extends Runnable {

    /**
     * 子线程执行的内容
     *
     * @see Runnable#run()
     * @see java.util.concurrent.Callable#call()
     */
    T doCall() throws Exception;

    /**
     * 子线程执行完毕的的结果
     */
    T getTaskResult();

    /**
     * 作为守护线程启动
     */
    default void runAsDaemonThread() {
        Thread dt = new Thread(this);
        dt.setDaemon(true);
        dt.start();
    }

}
