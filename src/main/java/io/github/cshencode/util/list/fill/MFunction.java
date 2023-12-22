package io.github.cshencode.util.list.fill;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/12/5
 */
@FunctionalInterface
public interface MFunction<T, R> extends SFunction<T, R>, Function<T, R>, Serializable {
}
