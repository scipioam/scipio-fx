package com.github.scipioam.scipiofx.framework.exception;

/**
 * @since 2022/6/24
 */
public class ConfigLoadException extends FrameworkException {
    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigLoadException(Throwable cause) {
        super(cause);
    }
}
