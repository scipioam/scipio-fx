package com.github.scipioam.scipiofx.framework.exception;

/**
 * @since 2022/6/24
 */
public class ResourceException extends FrameworkException {

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }
}
