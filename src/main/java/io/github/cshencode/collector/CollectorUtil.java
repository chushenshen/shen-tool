package io.github.cshencode.collector;

import io.github.cshencode.function.ListFunction;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
     * @param stream 流
     * @param action action
     * @param <T>    泛型
     * @return 返回入参流
     */
    @Deprecated
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

    public static <T> Stream<T> peekI(Stream<T> stream, BiConsumer<? super T, Integer> action) {
        Objects.requireNonNull(stream);
        Objects.requireNonNull(action);
        final int[] index = {0};
        return stream.peek(v -> {
            action.accept(v, index[0]);
            index[0]++;
        });
    }

    /**
     * 按照key进行分组操作
     *
     * @param keyMapper skuFunction
     * @param <T>       类泛型
     * @param <K>       无
     * @return 返回值
     */
    public static <T, K>
    Collector<T, ?, Map<K, List<T>>> group(Function<T, K> keyMapper) {
        return Collectors.groupingBy(keyMapper);
    }


    /**
     * list转map
     * 功能等同于 ：groupAndFist 方法，效率会高一点
     *
     * @param keyMapper 如果key重复，默认取最后一个
     * @param <T>       无
     * @param <K>       无
     * @return 无
     */
    public static <T, K>
    Collector<T, ?, Map<K, T>> toMap(Function<T, K> keyMapper) {
        return toMap2(keyMapper);
    }

    public static <T, K>
    Collector<T, ?, Map<K, T>> toMap1(Function<T, K> keyMapper) {
        return Collectors.toMap(keyMapper, Function.identity(), (u1, u2) -> u1);
    }

    public static <T, K>
    Collector<T, ?, Map<K, T>> toMap2(Function<T, K> keyMapper) {
        return Collectors.toMap(keyMapper, Function.identity(), (u1, u2) -> u2);
    }

    /**
     * list转map
     * 功能等同于 ：groupAndFist 方法，效率会高一点
     *
     * @param keyMapper 如果key重复，默认取最后一个
     * @param <T>       无
     * @param <K>       无
     * @return 无
     */
    public static <T, K, U>
    Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper,
                                     Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper, (u1, u2) -> u2);
    }

    /**
     * 分组后取每组的第一个
     *
     * @param classifier 无
     * @param <T>        无
     * @param <K>        无
     * @return 无
     */
    public static <T, K>
    Collector<T, ?, Map<K, T>> groupAndFist(Function<T, K> classifier) {
        return Collectors.groupingBy(classifier,
                Collectors.collectingAndThen(Collectors.toList(), ListFunction.listFirst()));
    }

    /**
     * 分组后排序
     *
     * @param classifier     无
     * @param sortComparator 排序方法
     * @param <T>            无
     * @param <K>            无
     * @return 无
     */
    public static <T, K>
    Collector<T, ?, Map<K, List<T>>> groupAndSort(Function<T, K> classifier,
                                                  Comparator<T> sortComparator) {
        return Collectors.groupingBy(classifier,
                Collectors.collectingAndThen(Collectors.toList(), list -> {
                    list.sort(sortComparator);
                    return list;
                }));
    }

    /**
     * 默认分组成list
     *
     * @param classifier 无
     * @param finisher   无
     * @param <T>        无
     * @param <K>        无
     * @param <RR>       无
     * @return 无
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
     * @param classifier 无
     * @param finisher   无
     * @param <T>        无
     * @param <K>        无
     * @param <RR>       无
     * @return 无
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
     * @param classifier 无
     * @param downstream 无
     * @param finisher   无
     * @param <T>        主键的字段
     * @param <K>        主键的值
     * @param <A>        每组中的一个值的数据格式
     * @param <D>        分组之后的列表值
     * @param <RR>       分组之后处理完每组之后每组返回的值
     * @return 无
     */
    public static <T, K, A, D, RR>
    Collector<T, ?, Map<K, RR>> groupAndThen(Function<? super T, ? extends K> classifier,
                                             Collector<T, A, D> downstream,
                                             Function<D, RR> finisher) {
        return Collectors.groupingBy(classifier,
                Collectors.collectingAndThen(downstream, finisher));
    }

    /**
     * 向上强转
     *
     * @param list
     * @param <T>  入参
     * @param <R>  出参
     * @return
     */
    public static <T extends R, R> Collection<R> listCast(Collection<T> list) {
        return listCast(list, null);
    }

    /**
     * 向上强转
     *
     * @param list
     * @param <T>  入参
     * @param <R>  出参
     * @return
     */
    public static <T extends R, R> Collection<R> listCast(Collection<T> list, Consumer<T> consumer) {
        return list.stream().peek(v -> {
            if (consumer != null) {
                consumer.accept(v);
            }
        }).collect(Collectors.toList());
    }

    public static <T extends R, R> List<R> listCast(List<T> list) {
        return listCast(list, null);
    }

    public static <T extends R, R> List<R> listCast(List<T> list, Consumer<T> consumer) {
        return list.stream().peek(v -> {
            if (consumer != null) {
                consumer.accept(v);
            }
        }).collect(Collectors.toList());
    }

    // TODO 两层嵌套list合并 {by css} 2021/08/05 14:39:05

}
