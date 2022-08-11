package io.github.cshencode.function;

@FunctionalInterface
public interface Function1<T, R> {
    R apply(T t);
}
