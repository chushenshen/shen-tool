package io.github.cshencode.exception;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/3/23
 */
public class RuntimeCustomException extends RuntimeException {
    private Object data;

    public <T> T getData() {
        return (T) data;
    }

    public RuntimeCustomException setData(Object data) {
        this.data = data;
        return this;
    }

    public RuntimeCustomException() {
    }

    public RuntimeCustomException(String message) {
        super(message);
    }

    public RuntimeCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeCustomException(Throwable cause) {
        super(cause);
    }

    public RuntimeCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
