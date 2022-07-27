package com.github.ScipioAM.scipio_fx.exception;

/**
 * 转换异常
 * @since 2022/7/27
 */
public class ConvertException extends JFXException{
    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }
}
