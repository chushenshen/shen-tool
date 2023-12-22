package io.github.cshencode.function;

import java.io.Serializable;

@FunctionalInterface
public interface Function2<T1, T2, R> extends Serializable {
    R apply(T1 t1, T2 t2);
}
