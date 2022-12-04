package io.github.cshencode.listcompare;

import com.alibaba.fastjson.JSON;
import io.github.cshencode.util.ListCompareUtil;
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
                .whenInsert(v -> {
                    System.out.println("新增：" + JSON.toJSONString(v));
                })
                .whenUpdate(v -> {
                    System.out.println("更新：" + JSON.toJSONString(v));
                })
                .whenRemove(v -> {
                    System.out.println("删除：" + JSON.toJSONString(v));
                })
                .compare();
    }
}
