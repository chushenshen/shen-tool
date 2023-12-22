package io.github.cshencode.util.list;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2023/6/15
 */
public class ListToMapUtil {

    /**
     * 如果list 是空，返回空map
     *
     * @param tService
     * @param keyMapper
     * @param <T>
     * @param <K>
     * @return
     */
//    public static <T, K> Map<K, T> queryListToMap(Collection<K> kList, IService<T> tService, SFunction<T, K> keyMapper) {
//        if (CollectionUtils.isEmpty(kList)) {
//            return new HashMap<>();
//        }
//        List<T> list = tService
//                .lambdaQuery()
//                .in(keyMapper, kList)
//                .list();
//        if (CollectionUtils.isEmpty(list)) {
//            return new HashMap<>();
//        }
//        return list.stream().collect(CollectorUtil.toMap(keyMapper));
//    }
}
