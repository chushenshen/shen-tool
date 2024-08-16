package io.github.cshencode;

import io.github.cshencode.collector.CollectorUtil;
import io.github.cshencode.stream.StreamUtil;

import java.util.Collection;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2023/11/9
 */
public class $ {
    public static <T> StreamUtil<T> stream(Collection<T> collection) {
        return StreamUtil.streamUtil(collection);
    }

    public static <T> StreamUtil<T> listFill(Collection<T> collection) {
        return StreamUtil.streamUtil(collection);
    }

    public static <T extends R, R> Collection<R> listCast(Collection<T> collection) {
        return CollectorUtil.listCast(collection);
    }

}
