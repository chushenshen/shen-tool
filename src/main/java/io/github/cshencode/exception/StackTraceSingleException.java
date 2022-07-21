package io.github.cshencode.exception;

/**
 * <p>
 * -  没有错误栈的 单例异常
 * </p>
 *
 * @author css
 * @since 2022/3/20
 */
public class StackTraceSingleException extends RuntimeCustomException {

    /**
     * 维护msg信息
     */
    public final ThreadLocal<String> msgThreadLocal = new ThreadLocal<>();
    private final static StackTraceSingleException INSTANCE = new StackTraceSingleException();

    private StackTraceSingleException() {
        super(null, null, true, false);
    }

    public static StackTraceSingleException msg(String msg) {
        INSTANCE.msgThreadLocal.set(msg);
        return INSTANCE;
    }

    @Override
    public String getMessage() {
        return msgThreadLocal.get();
    }
}