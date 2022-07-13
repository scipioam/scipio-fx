package com.github.ScipioAM.scipio_fx.utils;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 序列化的{@link Function}
 *
 * @param <P> 入参
 * @param <R> 返回值
 * @since 2022/7/8
 */
@FunctionalInterface
public interface SeFunction<P, R> extends Serializable, Function<P, R>, SeFunctional {

}
