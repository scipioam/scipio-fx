package com.github.scipioam.scipiofx.framework.concurrent;

/**
 * @author Alan Scipio
 * created on 2024/1/18
 */
public interface SimpleOnFinishListener<T> {

    void handle(T data);

}
