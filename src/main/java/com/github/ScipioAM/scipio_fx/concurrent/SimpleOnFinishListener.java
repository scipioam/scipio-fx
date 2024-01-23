package com.github.ScipioAM.scipio_fx.concurrent;

/**
 * @author Alan Scipio
 * created on 2024/1/18
 */
public interface SimpleOnFinishListener<T> {

    void handle(T data);

}
