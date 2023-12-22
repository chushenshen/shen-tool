package io.github.cshencode.function;

import java.io.Serializable;

@FunctionalInterface
public interface Function0<R> extends Serializable {
    R apply();
}
