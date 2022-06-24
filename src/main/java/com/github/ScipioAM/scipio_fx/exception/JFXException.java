package com.github.ScipioAM.scipio_fx.exception;

/**
 * @since 2022/6/24
 */
public class JFXException extends RuntimeException{

    public JFXException(String message) {
        super(message);
    }

    public JFXException(String message, Throwable cause) {
        super(message, cause);
    }

    public JFXException(Throwable cause) {
        super(cause);
    }
}
