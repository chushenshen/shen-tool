package io.github.cshencode.exception;

/**
 * <p>
 * -
 * </p>
 *
 * @author css
 * @since 2022/3/23
 */
public class ShenToolException extends RuntimeException {


    public ShenToolException() {
    }

    public ShenToolException(String message) {
        super(message);
    }

    public ShenToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShenToolException(Throwable cause) {
        super(cause);
    }

    public ShenToolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
