package io.github.cshencode.util.list.fill;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/11/22
 */
public class ListFillProperty<SOURCE, TARGET, I> {

    public ListFillProperty(MFunction<TARGET, I> targetGetFunction, MFunction<SOURCE, ?> sourceGetFunction) {
        this.childrenList = new ArrayList<>();
        this.targetGetFunction = targetGetFunction;
        this.sourceGetFunction = sourceGetFunction;
    }

    /**
     * 数据库查出来的数据，转map
     */
    private Map<I, TARGET> dbValueMap;

    private List<MFunction<ListFillUtil<TARGET>, ListFillUtil<TARGET>>> childrenList;
    /**
     * 提供orm查询，下面二选一
     */
    private IService<TARGET> ormService;
    // 回调外部进行查询
    private MFunction<List, List<TARGET>> ormFunction;

    // 自定义对比方法，兼容类型不一致的情况
    private MFunction<Object, I> sourceValueGetHandler;

    // 对比的get方法
    private MFunction<TARGET, I> targetGetFunction;
    private MFunction<SOURCE, ?> sourceGetFunction;
    // 转换字段
    private List<ConvertGroup<SOURCE, TARGET, I>> convertGroupList;

    private boolean selectAll = false;

    public ListFillProperty<SOURCE, TARGET, I> addChildren(MFunction<ListFillUtil<TARGET>, ListFillUtil<TARGET>> children) {
        childrenList.add(children);
        return this;
    }

    public Map<I, TARGET> getDbValueMap() {
        return dbValueMap;
    }

    public ListFillProperty<SOURCE, TARGET, I> setDbValueMap(Map<I, TARGET> dbValueMap) {
        this.dbValueMap = dbValueMap;
        return this;
    }

    public List<MFunction<ListFillUtil<TARGET>, ListFillUtil<TARGET>>> getChildrenList() {
        return childrenList;
    }

    public ListFillProperty<SOURCE, TARGET, I> setChildrenList(List<MFunction<ListFillUtil<TARGET>, ListFillUtil<TARGET>>> childrenList) {
        this.childrenList = childrenList;
        return this;
    }

    public IService<TARGET> getOrmService() {
        return ormService;
    }

    public ListFillProperty<SOURCE, TARGET, I> setOrmService(IService<TARGET> ormService) {
        this.ormService = ormService;
        return this;
    }

    public MFunction<List, List<TARGET>> getOrmFunction() {
        return ormFunction;
    }

    public ListFillProperty<SOURCE, TARGET, I> setOrmFunction(MFunction<List, List<TARGET>> ormFunction) {
        this.ormFunction = ormFunction;
        return this;
    }

    public MFunction<Object, I> getSourceValueGetHandler() {
        return sourceValueGetHandler;
    }

    public ListFillProperty<SOURCE, TARGET, I> setSourceValueGetHandler(MFunction<Object, I> sourceValueGetHandler) {
        this.sourceValueGetHandler = sourceValueGetHandler;
        return this;
    }

    public MFunction<TARGET, I> getTargetGetFunction() {
        return targetGetFunction;
    }

    public ListFillProperty<SOURCE, TARGET, I> setTargetGetFunction(MFunction<TARGET, I> targetGetFunction) {
        this.targetGetFunction = targetGetFunction;
        return this;
    }

    public MFunction<SOURCE, ?> getSourceGetFunction() {
        return sourceGetFunction;
    }

    public ListFillProperty<SOURCE, TARGET, I> setSourceGetFunction(MFunction<SOURCE, ?> sourceGetFunction) {
        this.sourceGetFunction = sourceGetFunction;
        return this;
    }

    public List<ConvertGroup<SOURCE, TARGET, I>> getConvertGroupList() {
        return convertGroupList;
    }

    public ListFillProperty<SOURCE, TARGET, I> setConvertGroupList(List<ConvertGroup<SOURCE, TARGET, I>> convertGroupList) {
        this.convertGroupList = convertGroupList;
        return this;
    }

    /**
     * 查询全部列
     */
    public ListFillProperty<SOURCE, TARGET, I> selectAll() {
        this.selectAll = true;
        return this;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
}
