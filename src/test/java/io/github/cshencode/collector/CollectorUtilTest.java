package io.github.cshencode.collector;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2021/8/2
 */
public class CollectorUtilTest {
    List<TestBean> testBeanList = new ArrayList<>();

    @Before
    public void testBefore() {
        testBeanList.add(new TestBean(1L, "one"));
        testBeanList.add(new TestBean(3L, "three"));
        testBeanList.add(new TestBean(4L, "four"));
        testBeanList.add(new TestBean(4L, "four1"));
        testBeanList.add(new TestBean(2L, "two"));
    }

    @After
    public void testAfter() {
    }

    @Test
    public void groupAndFirst() {
        Stream<TestBean> stream = testBeanList.stream();
        Map<Long, TestBean> collect = stream
                .collect(CollectorUtil.groupAndFist(TestBean::getId));
        System.out.println(JSON.toJSONString(collect));
    }

    @Test
    public void groupAndSort() {
        // 分组，组内通过id排序
        Map<Long, List<TestBean>> collect = testBeanList.stream()
                .collect(CollectorUtil.groupAndSort(TestBean::getId, (v1, v2) -> {
                    //倒叙
                    return (int) (v2.getId() - v1.getId());
                }));
        System.out.println(JSON.toJSONString(collect));
    }

    @Test
    public void groupAndThen() {
        Map<Long, TestBean> key = testBeanList.stream()
                .collect(CollectorUtil.groupAndThen(TestBean::getId,
                        Collectors.toList(),
                        list -> list.get(0)));
        System.out.println(key);
    }

    @Test
    public void groupAndListThen() {
        Map<Long, TestBean> key = testBeanList.stream()
                .collect(CollectorUtil.groupAndListThen(TestBean::getId,
                        list -> list.get(0)));
        System.out.println(key);
    }

    @Test
    public void groupAndSetThen() {
        Map<Long, TestBean> key = testBeanList.stream()
                .collect(CollectorUtil.groupAndSetThen(TestBean::getId,
                        list -> list.iterator().next()));
        System.out.println(key);

    }

    @Test
    public void collectorGroupAndThen() {
        Map<Object, String> key = testBeanList.stream()
                .collect(Collectors.groupingBy(
                        TestBean::getId, Collectors.collectingAndThen(Collectors.toList(), list -> {
                            return "";
                        })
                ));
        System.out.println(key);
    }

    @Test
    public void foreachI() {
        CollectorUtil.forEachI(testBeanList.stream(), (v, i) -> {
            System.out.println(v);
            System.out.println(i);
        });
    }

    @Test
    public void peekI() {
        long count = CollectorUtil.peekI(testBeanList.stream(), (v, i) -> {
            System.out.println(v);
            System.out.println(i);
        }).count();
    }
}
