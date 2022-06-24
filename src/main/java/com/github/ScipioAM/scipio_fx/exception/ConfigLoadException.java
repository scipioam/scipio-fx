package com.github.ScipioAM.scipio_fx.exception;

/**
 * @since 2022/6/24
 */
public class ConfigLoadException extends JFXException {
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
