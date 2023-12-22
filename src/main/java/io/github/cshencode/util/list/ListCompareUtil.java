package io.github.cshencode.util.list;


import net.sf.cglib.beans.BeanCopier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * * 数据对比器
 *
 * @author css
 * @param <Entity> 实体类的类型
 * @param <IdType> 实体类id的类型
 */
public class ListCompareUtil<Entity, IdType> {
    private final static ConcurrentHashMap<Class<?>, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private final Collection<Entity> oldenList;
    private final Collection<Entity> newlyList;
    /**
     * 数组回调
     */
    private Consumer<List<Entity>> updateListConsumer;
    private Consumer<List<Entity>> insertListConsumer;
    private Consumer<Set<IdType>> idRemoveConsumer;
    /**
     * 单个回调
     */
    private Function<Entity, ?> updateOneFunction;
    private Function<Entity, ?> insertOneFunction;
    private Function<IdType, ?> idOneFunction;
    private Consumer<Collection<Entity>> removeListConsumer;
    private Function<Entity, IdType> idGetter;

    private ListCompareUtil(Collection<Entity> oldenList, Collection<Entity> newlyList, Function<Entity, IdType> idGetter) {
        this.oldenList = Optional.ofNullable(oldenList).orElse(new ArrayList<>());
        this.newlyList = Optional.ofNullable(newlyList).orElse(new ArrayList<>());
        this.idGetter = idGetter;
    }

    public ListCompareUtil<Entity, IdType> idGetter(Function<Entity, IdType> idGetter) {
        this.idGetter = idGetter;
        return this;
    }

    /**
     * 当有更新数据发生时
     * @param updateListConsumer
     * @return
     */
    public ListCompareUtil<Entity, IdType> whenUpdate(Consumer<List<Entity>> updateListConsumer) {
        this.updateListConsumer = updateListConsumer;
        return this;
    }

    /**
     * 当有新数据发生时
     * @param insertListConsumer
     * @return
     */
    public ListCompareUtil<Entity, IdType> whenInsert(Consumer<List<Entity>> insertListConsumer) {
        this.insertListConsumer = insertListConsumer;
        return this;
    }

    /**
     * 当有删除数据发生时
     * @param idRemoveConsumer
     * @return
     */
    public ListCompareUtil<Entity, IdType> whenRemove(Consumer<Set<IdType>> idRemoveConsumer) {
        this.idRemoveConsumer = idRemoveConsumer;
        return this;
    }

    public ListCompareUtil<Entity, IdType> whenUpdateOne(Function<Entity, ?> updateOneFunction) {
        this.updateOneFunction = updateOneFunction;
        return this;
    }

    public ListCompareUtil<Entity, IdType> whenInsertOne(Function<Entity, ?> insertOneFunction) {
        this.insertOneFunction = insertOneFunction;
        return this;
    }

    public ListCompareUtil<Entity, IdType> whenRemoveOne(Function<IdType, ?> idOneFunction) {
        this.idOneFunction = idOneFunction;
        return this;
    }

    public ListCompareUtil<Entity, IdType> whenRemoveEntity(Consumer<Collection<Entity>> removeListConsumer) {
        this.removeListConsumer = removeListConsumer;
        return this;
    }

    /**
     * 构建比较器
     *
     * @param oldenList
     * @param newlyList
     * @param idGetter
     * @param <T>
     * @param <I>
     * @return 返回构建的工具类
     */
    public static <T, I> ListCompareUtil<T, I> builder(Collection<? extends T> oldenList,
                                                       Collection<? extends T> newlyList,
                                                       Function<T, I> idGetter) {
        return new ListCompareUtil(oldenList, newlyList, idGetter);
    }

    /**
     * 开始对比新老数据
     */
    public void compare() {
        if (oldenList.isEmpty() && newlyList.isEmpty()) {
            return;
        }

        List<Entity> insertList = new ArrayList<>();
        List<Entity> tmpNewlyList = new ArrayList<>();
        for (Entity newEntity : newlyList) {
            if (idGetter.apply(newEntity) == null) {
                insertList.add(newEntity);
            } else {
                tmpNewlyList.add(newEntity);
            }
        }
        //通过ID映射数据
        Map<IdType, Entity> oldenMap = this.oldenList.stream().collect(Collectors.toMap(idGetter, Function.identity(), (f, s) -> f, LinkedHashMap::new));
        Map<IdType, Entity> newlyMap = tmpNewlyList.stream().collect(Collectors.toMap(idGetter, Function.identity(), (f, s) -> f, LinkedHashMap::new));

        Set<IdType> oldenKeys = oldenMap.keySet();
        Set<IdType> newlyKeys = newlyMap.keySet();

        //获取需要更新的数据, oldenMap中存在newlyMap中的key，则需要更新
        //获取需要新增的数据，newlyMap中不存在与oldenMap中的，则需要添加
        //获取需要删除的数据，oldenMap中不存在与newlyMap中的，则需要删除
        List<Entity> updateList = new ArrayList<>();
        Set<IdType> idRemoves = new HashSet<>();
        List<Entity> removeList = new ArrayList<>();

        oldenKeys.forEach(oldenKey -> {
            //旧key存在于新key中，则需要更新
            if (newlyKeys.contains(oldenKey)) {
                Entity olden = oldenMap.get(oldenKey);
                Entity newly = newlyMap.get(oldenKey);
                BeanCopier beanCopier = BEAN_COPIER_MAP.computeIfAbsent(newly.getClass(), key ->
                        BeanCopier.create(key, key, false));
                beanCopier.copy(newly, olden, null);
                updateList.add(olden);
            } else {//不存在则需要删除
                idRemoves.add(oldenKey);
                removeList.add(oldenMap.get(oldenKey));
            }
        });

        newlyKeys.forEach(newlyKey -> {
            //旧key不包含则需要新增
            if (!oldenKeys.contains(newlyKey)) {
                insertList.add(newlyMap.get(newlyKey));
            }
        });

        // 回调更新
        if (!updateList.isEmpty()) {
            if (updateOneFunction != null) {
                for (Entity updateEntity : updateList) {
                    updateOneFunction.apply(updateEntity);
                }
            } else if (updateListConsumer != null) {
                updateListConsumer.accept(updateList);
            }
        }

        // 回调插入
        if (!insertList.isEmpty()) {
            if (insertOneFunction != null) {
                for (Entity insertEntity : insertList) {
                    insertOneFunction.apply(insertEntity);
                }
            } else if (insertListConsumer != null) {
                insertListConsumer.accept(insertList);
            }
        }

        // 回调删除
        if (!idRemoves.isEmpty()) {
            if (idOneFunction != null) {
                for (IdType id : idRemoves) {
                    idOneFunction.apply(id);
                }
            } else if (idRemoveConsumer != null) {
                idRemoveConsumer.accept(idRemoves);
            } else if (removeListConsumer != null) {
                removeListConsumer.accept(removeList);
            }
        }
    }

}
