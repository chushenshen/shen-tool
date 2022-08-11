package io.github.cshencode.collector;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    List<HashMap<String, Object>> hashMapList = new ArrayList<>();

    @Before
    public void testBefore() {
        hashMapList.add(getMap("key", 1));
        hashMapList.add(getMap("key", 2));
    }

    public HashMap<String, Object> getMap(String key, Object val) {
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put(key, val);
        return hashMap;
    }

    @After
    public void testAfter() {
    }

    @Test
    public void groupAndFirst() {
        Stream<HashMap<String, Object>> stream = hashMapList.stream();
        Map<?, HashMap<String, ?>> collect = stream
                .collect(CollectorUtil.groupAndFist(v -> v.get("key")));
        System.out.println(JSON.toJSONString(collect));
    }

    @Test
    public void groupAndSort() {
        List<HashMap<String, Object>> sortMapTest = new ArrayList<>();
        HashMap<String, Object> map = getMap("key", "one");
        map.put("num", 2);
        sortMapTest.add(map);
        map = getMap("key", "one");
        map.put("num", 1);
        sortMapTest.add(map);

        map = getMap("key", "two");
        map.put("num", 3);
        sortMapTest.add(map);
        map = getMap("key", "two");
        map.put("num", 5);
        sortMapTest.add(map);

        Map<Object, List<HashMap<String, Object>>> collect = sortMapTest.stream()
                .collect(CollectorUtil.groupAndSort(v -> v.get("key"), (v1, v2) -> {
                    //倒叙
                    return (Integer) v2.get("num") - (Integer) v1.get("num");
                }));
        System.out.println(JSON.toJSONString(collect));
    }

    @Test
    public void groupAndThen() {
        Map<Object, HashMap<String, Object>> key = hashMapList.stream()
                .collect(CollectorUtil.groupAndThen(v -> v.get("key"),
                        Collectors.toList(),
                        list -> list.get(0)));
        System.out.println(key);
    }

    @Test
    public void groupAndListThen() {
        Map<Object, HashMap<String, Object>> key = hashMapList.stream()
                .collect(CollectorUtil.groupAndListThen(v -> v.get("key"),
                        list -> list.get(0)));
        System.out.println(key);
    }

    @Test
    public void groupAndSetThen() {
        Map<Object, HashMap<String, Object>> key = hashMapList.stream()
                .collect(CollectorUtil.groupAndSetThen(v -> v.get("key"),
                        list -> list.iterator().next()))
                ;
        System.out.println(key);

    }

    @Test
    public void collectorGroupAndThen() {
        Map<Object, String> key = hashMapList.stream()
                .collect(Collectors.groupingBy(
                        v -> v.get("key"), Collectors.collectingAndThen(Collectors.toList(), list -> {
                            return "";
                        })
                ));
        System.out.println(key);
    }

    @Test
    public void foreachI() {
        CollectorUtil.forEachI(hashMapList.stream(), (v, i)->{
            System.out.println(v);
            System.out.println(i);
        });
    }
}
