package com.github.ScipioAM.scipio_fx.concurrent;

import lombok.Getter;
import lombok.Setter;

/**
 * UI回调
 *
 * @author Alan Scipio
 * created on 2024/1/18
 */
@Getter
@Setter
public abstract class SimpleUiCallback<T> implements Runnable {

    private T data;

    @Override
    public void run() {
        execute(data);
    }

    public abstract void execute(T data);

}
