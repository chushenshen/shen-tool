package io.github.cshencode.exception;

import org.junit.Test;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/3/23
 */
public class ExceptionTest {
    static StackTraceSingleException singleStackTraceException;

    @Test
    public void test() {
        System.out.println(1);
        try {
            throw StackTraceSingleException.msg("测试1").setData("haha");
        } catch (StackTraceSingleException e) {
            String data = e.getData();
            System.out.println(data);
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            throw StackTraceSingleException.msg("测试2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNature() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            try {
                throw new RuntimeException("sss");
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            try {
                throw StackTraceSingleException.msg("测试2");
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
