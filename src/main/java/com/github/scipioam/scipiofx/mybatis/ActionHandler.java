package com.github.scipioam.scipiofx.mybatis;

/**
 * DB操作
 *
 * @author Alan Scipio
 * created on 2023/1/13
 */
@FunctionalInterface
public interface ActionHandler<T> {

    void handle(T mapper);

}
