package io.github.cshencode.exception;

/**
 * <p>
 * -  没有错误栈的异常，之有message
 * 构建错误对象更高效
 * <br/>缺点：没有错误栈（只用于抛出异常、或者回滚事物）
 * </p>
 *
 * @author css
 * @since 2022/3/20
 * @see java.lang.RuntimeException
 */
public class StackTraceNoException extends RuntimeException {
    private StackTraceNoException(String msg) {
        super(msg, null, true, false);
    }

    public static StackTraceNoException msg(String msg) {
        return new StackTraceNoException(msg);
    }

}