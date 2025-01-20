package com.github.scipioam.scipiofx.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alan Scipio
 * created on 2025-01-17
 */
public class LogHelper {

    private final Logger slf4jLogger;

    public LogHelper(Class<?> clazz) {
        this.slf4jLogger = clazz == null ? null : LoggerFactory.getLogger(clazz);
    }

    private void printMsg(boolean isStdOut, String msg, Object... args) {
        String finalMsg = buildMsg(msg, args);
        if (isStdOut) {
            System.out.println(finalMsg);
        } else {
            System.err.println(finalMsg);
        }
    }

    private String buildMsg(String msg, Object... args) {
        if (args != null) {
            for (Object arg : args) {
                msg = msg.replaceFirst("\\{}", arg.toString());
            }
        }
        return msg;
    }

    public void info(String msg, Object... args) {
        if (slf4jLogger != null) {
            slf4jLogger.info(msg, args);
        } else {
            printMsg(true, msg, args);
        }
    }

    public void info(String msg) {
        if (slf4jLogger != null) {
            slf4jLogger.info(msg);
        } else {
            printMsg(true, msg);
        }
    }

    public void error(String msg, Object... args) {
        if (slf4jLogger != null) {
            slf4jLogger.error(msg, args);
        } else {
            printMsg(false, msg, args);
        }
    }

    public void error(String msg) {
        if (slf4jLogger != null) {
            slf4jLogger.error(msg);
        } else {
            printMsg(false, msg);
        }
    }

    public void warn(String msg, Object... args) {
        if (slf4jLogger != null) {
            slf4jLogger.warn(msg, args);
        } else {
            printMsg(false, msg, args);
        }
    }

    public void warn(String msg) {
        if (slf4jLogger != null) {
            slf4jLogger.warn(msg);
        } else {
            printMsg(false, msg);
        }
    }

    public void debug(String msg, Object... args) {
        if (slf4jLogger != null) {
            slf4jLogger.debug(msg, args);
        } else {
            printMsg(true, msg, args);
        }
    }

    public void debug(String msg) {
        if (slf4jLogger != null) {
            slf4jLogger.debug(msg);
        } else {
            printMsg(true, msg);
        }
    }

}
