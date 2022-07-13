package com.github.ScipioAM.scipio_fx.utils;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 序列化的{@link Supplier}
 *
 * @param <R> 返回值
 * @since 2022/7/13
 */
public interface SeSupplier<R> extends Serializable, Supplier<R>, SeFunctional {
}
