package io.github.cshencode.util.list.fill;


import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.cshencode.collector.CollectorUtil;
import io.github.cshencode.exception.ShenToolException;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * <p>
 * -  填充属性
 * </p>
 *
 * @author css
 * @since 2022/11/22
 */
public class ListFillUtil<Entity> {

    private Collection<? extends Entity> list;

    //需要转换的对比字段列表
    private List<ListFillProperty> propertyList;

    private static final Map<SFunction, Object> LAMBDA_CACHE = new ConcurrentHashMap<>();
    private static String MYBATIS_PLUS_VERSION = null;

    public ListFillUtil(Collection<? extends Entity> list) {
        this.list = list;
        propertyList = new ArrayList<>();
    }

    public static <T> ListFillUtil<T> builder(Collection<? extends T> list) {
        return new ListFillUtil<>(list);
    }

    /**
     * @param listFillProperty 转换时的对比字段
     * @param convertGroups    需要转换的字段
     * @param <T>
     * @param <I>
     * @return
     */
    public <T, I> ListFillUtil<Entity> addProperty(ListFillProperty<Entity, T, I> listFillProperty,
                                                   ConvertGroup... convertGroups) {
        propertyList.add(listFillProperty.setConvertGroupList(newArrayList(convertGroups)));

        List queryParamList = new ArrayList<>();
        for (Entity entity : list) {
            // 取出原始key
            Object apply = listFillProperty.getSourceGetFunction().apply(entity);
            if (apply != null) {
                queryParamList.add(apply);
            }
        }

        if (CollectionUtils.isEmpty(queryParamList)) {
            return this;
        }
        IService<T> ormService = listFillProperty.getOrmService();
        MFunction<List, List<T>> ormFunction = listFillProperty.getOrmFunction();
        List<T> dbList;
        if (ormService != null) {
            // 查询数据库

            LambdaQueryWrapper<T> queryChainWrapper = new LambdaQueryWrapper<T>()
//                    .eq(BaseEntity::getIsDelete, 0)
                    .in(listFillProperty.getTargetGetFunction(), queryParamList);
            List<ConvertGroup<Entity, T, I>> convertGroupList = listFillProperty.getConvertGroupList();
            // 查询列，只查需要转换的列
            // 查询列，只查需要转换的列
            if (!listFillProperty.isSelectAll()) {
                List<SFunction<T, ?>> selectFieldList = new ArrayList<>();
                selectFieldList.add(listFillProperty.getTargetGetFunction());
                for (ConvertGroup<Entity, T, I> convertGroup : convertGroupList) {
                    if (hasColumn(convertGroup.getTargetNameGetFunction())) {
                        selectFieldList.add(convertGroup.getTargetNameGetFunction());
                    }
                }

                queryChainWrapper.select(selectFieldList.toArray(new SFunction[]{}));
            }

            dbList = ormService.list(queryChainWrapper);
        } else if (ormFunction != null) {
            // 回调方法，让外部处理
            dbList = ormFunction.apply(queryParamList);
        } else {
            throw new RuntimeException("请设置ListFillProperty的 ormService 或者 ormFunction 参数");
        }

        if (dbList == null) {
            dbList = new ArrayList<>();
        }

        List<MFunction<ListFillUtil<T>, ListFillUtil<T>>> childrenList = listFillProperty.getChildrenList();
        if (childrenList != null) {
            // 调用子类
            for (MFunction<ListFillUtil<T>, ListFillUtil<T>> utilSFunction : childrenList) {
                utilSFunction.apply(ListFillUtil.builder(dbList));
            }
        }
        Map<I, T> valueMap = dbList.stream()
                .collect(CollectorUtil.toMap(listFillProperty.getTargetGetFunction()));
        listFillProperty.setDbValueMap(valueMap);
        // 进行转换
        for (Entity entity : list) {
            //
            Object entityGetValue = listFillProperty.getSourceGetFunction().apply(entity);

            // 处理器，针对两边类型不一致的情况，进行转换
            MFunction<Object, I> sourceValueGetHandler = listFillProperty.getSourceValueGetHandler();
            if (sourceValueGetHandler != null) {
                entityGetValue = sourceValueGetHandler.apply(entityGetValue);
            }

            T value = valueMap.get(entityGetValue);
            List<ConvertGroup<Entity, T, I>> convertGroupList = listFillProperty.getConvertGroupList();
            for (ConvertGroup<Entity, T, I> convertGroup : convertGroupList) {
                Object apply = null;
                if (value != null) {
                    apply = convertGroup.getTargetNameGetFunction().apply(value);
                } else {
                    BiFunction<Object, Map<I, T>, I> compareHandler = convertGroup.getCompareHandler();
                    if (compareHandler != null) {
                        apply = compareHandler.apply(entityGetValue, valueMap);
                    }
                }
                convertGroup.getSourceNameSetFunction().accept(entity, (I) apply);
            }
        }

        return this;
    }

    /**
     * 增加判断条件
     *
     * @param condition
     * @param listFillProperty
     * @param convertGroups
     * @param <T>
     * @param <I>
     * @return
     */
    public <T, I> ListFillUtil<Entity> addProperty(boolean condition, ListFillProperty<Entity, T, I> listFillProperty,
                                                   ConvertGroup... convertGroups) {
        if (condition) {
            addProperty(listFillProperty, convertGroups);
        }
        return this;
    }

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        // Avoid integer overflow when a large array is passed in
        ArrayList<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * 数据库中有没有这个字段
     *
     * @param func
     * @return
     * @throws MybatisPlusException
     */
    public boolean hasColumn(SFunction func) {

        Object stringStringMap = LAMBDA_CACHE.get(func);
        if (stringStringMap != null) {
            return true;
        }

        SerializedLambda lambda = extract(func);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        String instantiatedMethodType = lambda.getInstantiatedMethodType();
        Class aClass = ClassUtils.toClassConfident(instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";")).replace("/", "."));

        Class<LambdaUtils> lambdaUtilsClass = LambdaUtils.class;
        try {
            Object columnCache;
            String mybatisPlusVersion = getMybatisPlusVersion();
            if (mybatisPlusVersion != null && mybatisPlusVersion.startsWith("3.0")) {
                Method getColumnMap = lambdaUtilsClass.getMethod("getColumnMap", String.class);
                Map<String, String> columnMap = (Map<String, String>) getColumnMap.invoke(null, aClass.getSimpleName());
                if (columnMap == null || columnMap.isEmpty()) {
                    columnMap = (Map<String, String>) getColumnMap.invoke(null, aClass.getName());
                }
                columnCache = columnMap.get(fieldName);
            } else {
                Method getColumnMap = lambdaUtilsClass.getMethod("getColumnMap", Class.class);
                Map<String, Object> columnMap = (Map<String, Object>) getColumnMap.invoke(null, aClass);
                columnCache = columnMap.get(fieldName);
            }
            LAMBDA_CACHE.put(func, columnCache);
            return columnCache != null;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T> SerializedLambda extract(SFunction<T, ?> func) {
        if (func instanceof Proxy) {
//            return new IdeaProxyLambdaMeta((Proxy)func);
            return null;
        } else {
            try {
                Method method = func.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(true);
                java.lang.invoke.SerializedLambda invoke = (java.lang.invoke.SerializedLambda) method.invoke(func);
                return invoke;
            } catch (Throwable var2) {
                throw new ShenToolException(var2);
            }
        }
    }

    public static String getMybatisPlusVersion() {

        if (MYBATIS_PLUS_VERSION != null) {
            return MYBATIS_PLUS_VERSION;
        }

        try {
            Class.forName("com.baomidou.mybatisplus.core.MybatisPlusVersion");
            MYBATIS_PLUS_VERSION = MybatisPlusVersion.getVersion();
        } catch (ClassNotFoundException e) {
            return null;
        }
        return MYBATIS_PLUS_VERSION;
    }

}
