工具类-简化steam操作
通过简单的方式完成流 分组、循环

## 基础用法
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestBean {
    private Long id;
    private String name;

}

List<TestBean> testBeanList = new ArrayList<>();
testBeanList.add(new TestBean(1L, "one"));
testBeanList.add(new TestBean(3L, "three"));
testBeanList.add(new TestBean(4L, "four"));
testBeanList.add(new TestBean(4L, "four1"));
testBeanList.add(new TestBean(2L, "two"));


分组，每组取第一个
Stream<TestBean> stream = testBeanList.stream();
Map<Long, TestBean> collect = stream
.collect(CollectorUtil.groupAndFist(TestBean::getId));
System.out.println(JSON.toJSONString(collect));
输出结果：
{1:{"id":1,"name":"one"},2:{"id":2,"name":"two"},3:{"id":3,"name":"three"},4:{"id":4,"name":"four"}}


// 分组，组内通过id排序
Map<Long, List<TestBean>> collect = testBeanList.stream()
.collect(CollectorUtil.groupAndSort(TestBean::getId, (v1, v2) -> {
//倒叙
return (int) (v2.getId() - v1.getId());
}));
System.out.println(JSON.toJSONString(collect));
输出结果：
{1:[{"id":1,"name":"one"}],2:[{"id":2,"name":"two"}],3:[{"id":3,"name":"three"}],4:[{"id":4,"name":"four"},{"id":4,"name":"four1"}]}

带下标的peek
long count = CollectorUtil.peekI(testBeanList.stream(), (v, i) -> {
System.out.println(v);
System.out.println(i);
}).count();
```

## 列表对比
```java
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

```