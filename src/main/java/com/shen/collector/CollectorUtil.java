package com.shen.collector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * -  Collectors 简易操作工具类
 * </p>
 * {@link Collectors}
 *
 * @author css
 * @since 2021/8/2
 */
public class CollectorUtil {


    /**
     * 带有下标的foreach循环
     *
     * @param stream
     * @param action
     * @param <T>
     * @return
     */
    public static <T> Stream<T> forEachI(Stream<T> stream, BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(stream);
        Objects.requireNonNull(action);
        int index = 0;
        Iterator<T> iterator = stream.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            action.accept(index, next);
            index++;
        }
        return stream;
    }

    /**
     * 按照key进行分组操作
     *
     * @param getId
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K>
    Collector<T, ?, Map<K, List<T>>> group(Function<T, K> getId) {
        return Collectors.groupingBy(getId);
    }


    /**
     * list转map
     * 功能等同于 ：groupAndFist 方法，效率会高一点
     *
     * @param idFunction 需要确保id唯一，不然会抛出map的merge异常
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K>
    Collector<T, ?, Map<K, T>> toMap(Function<T, K> idFunction) {
        return Collectors.toMap(idFunction, Function.identity());
    }

    /**
     * 分组后取每组的第一个
     *
     * @param getId
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K>
    Collector<T, ?, Map<K, T>> groupAndFist(Function<T, K> getId) {
        return Collectors.groupingBy(getId,
                Collectors.collectingAndThen(Collectors.toList(), ListFunction.listFirst()));
    }

    /**
     * 分组后排序
     *
     * @param getId
     * @param sortComparator 排序方法
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K>
    Collector<T, ?, Map<K, List<T>>> groupAndSort(Function<T, K> getId,
                                                  Comparator<T> sortComparator) {
        return Collectors.groupingBy(getId,
                Collectors.collectingAndThen(Collectors.toList(), list -> {
                    list.sort(sortComparator);
                    return list;
                }));
    }

    /**
     * 默认分组成list
     *
     * @param classifier
     * @param finisher
     * @param <T>
     * @param <K>
     * @param <RR>
     * @return
     */
    public static <T, K, RR>
    Collector<T, ?, Map<K, RR>> groupAndListThen(Function<? super T, ? extends K> classifier,
                                                 Function<List<T>, RR> finisher) {
        return Collectors.groupingBy(classifier,
                Collectors.collectingAndThen(Collectors.toList(), finisher));
    }

    /**
     * 默认分组成Set
     *
     * @param classifier
     * @param finisher
     * @param <T>
     * @param <K>
     * @param <RR>
     * @return
     */
    public static <T, K, RR>
    Collector<T, ?, Map<K, RR>> groupAndSetThen(Function<? super T, ? extends K> classifier,
                                                Function<Set<T>, RR> finisher) {
        return Collectors.groupingBy(classifier,
                Collectors.collectingAndThen(Collectors.toSet(), finisher));
    }

    /**
     * 分组并且紧跟着做一些自定义操作
     *
     * @param classifier
     * @param finisher
     * @param <T>        主键的字段
     * @param <K>        主键的值
     * @param <A>        每组中的一个值的数据格式
     * @param <D>        分组之后的列表值
     * @param <RR>       分组之后处理完每组之后每组返回的值
     * @return
     */
    public static <T, K, A, D, RR>
    Collector<T, ?, Map<K, RR>> groupAndThen(Function<? super T, ? extends K> classifier,
                                             Collector<T, A, D> downstream,
                                             Function<D, RR> finisher) {
        return Collectors.groupingBy(classifier,
                Collectors.collectingAndThen(downstream, finisher));
    }

    public static <T, R> Collection<T> listCast(List<R> list) {
        return list.stream().map(v -> (T) v).collect(Collectors.toList());
    }

    // TODO 两层嵌套list合并 {by css} 2021/08/05 14:39:05

}
