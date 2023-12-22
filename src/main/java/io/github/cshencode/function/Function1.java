package io.github.cshencode.function;

import java.io.Serializable;

@FunctionalInterface
public interface Function1<T, R> extends Serializable {
    R apply(T t);
}
