package io.github.cshencode.stream;

import java.util.Collection;

/**
 * <p>
 * -  流工具类
 * </p>
 *
 * @author css
 * @since 2022/3/20
 */
public class StreamUtil<T> {

    Collection<T> collection;

    public StreamUtil(Collection<T> collection) {
        this.collection = collection;
    }

    public static <T> StreamUtil<T> streamUtil(Collection<T> collection) {
        return new StreamUtil<>(collection);
    }

    public <T> StreamUtil<T> toMap(Collection<T> collection) {

        return new StreamUtil<>(collection);
    }
}
