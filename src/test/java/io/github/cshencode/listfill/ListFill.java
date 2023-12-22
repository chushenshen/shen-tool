package io.github.cshencode.listfill;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.cshencode.listcompare.ListCompareBean;
import io.github.cshencode.util.list.fill.ListFillUtil;
import org.junit.Test;

import java.util.ArrayList;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/12/5
 */
public class ListFill {
    @Test
    public void test() {
    }

    public static void main(String[] args) {
        String s = ListFillUtil.getMybatisPlusVersion();
        SFunction<ListCompareBean, Long> getId = ListCompareBean::getId;
        new ListFillUtil(new ArrayList()).hasColumn(getId);
    }
}
