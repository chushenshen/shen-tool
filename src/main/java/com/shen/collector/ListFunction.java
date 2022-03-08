package com.shen.collector;

import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2021/8/2
 */
public interface ListFunction {
    static <T> Function<List<T>, T> listFirst() {
        return t -> t.get(0);
    }

    static <T> Function<List<T>, List<T>> list() {
        return t -> t;
    }
}
