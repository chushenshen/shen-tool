package io.github.cshencode.listcompare;

import com.alibaba.fastjson.JSON;
import io.github.cshencode.util.list.ListCompareUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/12/4
 */
public class ListCompareUtilTest {
    @Test
    public void simpleTest() {
        List<ListCompareBean> oldCompareBeanList = new ArrayList<>();
        oldCompareBeanList.add(new ListCompareBean(1L, "test1"));
        oldCompareBeanList.add(new ListCompareBean(2L, "test2"));

        List<ListCompareBean> newCompareBeanList = new ArrayList<>();
        newCompareBeanList.add(new ListCompareBean(1L, "updateTest1"));
        newCompareBeanList.add(new ListCompareBean(3L, "test3"));

        ListCompareUtil.builder(oldCompareBeanList, newCompareBeanList, ListCompareBean::getId)
                // 批量回调
                .whenInsert(list -> {
                    // 进行orm操作
                    System.out.println("新增：" + JSON.toJSONString(list));
                })
                .whenUpdate(list -> {
                    System.out.println("更新：" + JSON.toJSONString(list));
                })
                .whenRemove(list -> {
                    // id列表
                    System.out.println("删除：" + JSON.toJSONString(list));
                })
                .compare();

        ListCompareUtil.builder(oldCompareBeanList, newCompareBeanList, ListCompareBean::getId)
                // 单个回调，for循环挨个回调
                .whenInsertOne(one -> {
                    System.out.println("单个新增：" + JSON.toJSONString(one));
                    return 0;
                })
                .whenUpdateOne(one -> {
                    System.out.println("单个更新：" + JSON.toJSONString(one));
                    return 0;
                })
                .whenRemoveOne(one -> {
                    System.out.println("单个删除：" + JSON.toJSONString(one));
                    return 0;
                })
                .compare();
    }
}
