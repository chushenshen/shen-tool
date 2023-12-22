package io.github.cshencode;

import java.util.ArrayList;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2023/11/9
 */
public class ToolTest {
    @org.junit.Test
    public void test() {
        ArrayList<String> list = new ArrayList<>();
        System.out.println($.stream(list));
    }
}
