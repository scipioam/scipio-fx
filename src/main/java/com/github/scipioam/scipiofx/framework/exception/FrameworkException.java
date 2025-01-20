package com.github.scipioam.scipiofx.framework.exception;

/**
 * @since 2022/6/24
 */
public class FrameworkException extends RuntimeException{

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(Throwable cause) {
        super(cause);
    }
}
